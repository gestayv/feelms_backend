package ejb;

import facade.FilmFacade;
import model.Film;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by arturo on 13-05-17.
 */

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
@Singleton
public class SearcherEJB {

    private String msg = "nothing";

    private Directory ramDirectory = null;

    private IndexWriter indexWriter = null;

    private IndexReader indexReader = null;

    private IndexSearcher indexSearcher = null;

    private Analyzer analyzer = null;

    Logger logger = Logger.getLogger(SearcherEJB.class.getName());

    private boolean status = false;

    @EJB(name="FilmFacadeEJB")
    FilmFacade filmFacadeEJB;

    @PostConstruct
    public void init() {
        this.msg = "Correctly Done";
        try {
            start();
            this.status = true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            this.status = false;
        }

    }

    @PreDestroy
    public void destroy() {

        try {
            if(indexWriter != null && indexWriter.isOpen()) {
                this.indexWriter.close();
            }
            if(indexReader != null) {
                indexReader.close();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }

    public String testMsg() {
        return this.msg;
    }

    @Lock(LockType.WRITE)
    private int start() throws IOException {

        int numIndex = 0;

        //Le agregue CharrArraySet.empty_set para que cuente stopwords, como
        //hay nombres de peliculas que las usan.
        analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);
        //  revisar como se comporta el index, crearlo de nuevo siempre
        //  o utilizar append. Por ahora se creará de nuevo siempre.
        IndexWriterConfig indexconfig = new IndexWriterConfig(analyzer);
        indexconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        this.ramDirectory = new RAMDirectory();

        try {
            this.indexWriter = new IndexWriter(ramDirectory, indexconfig);

            List<Film> movies = filmFacadeEJB.findAll();

            for (Film m : movies) {
                Document doc = new Document();

                doc.add(new StoredField("film_id", m.getId()));
                if(m.getOriginalTitle() != null) {
                    doc.add(new TextField("original_title", m.getOriginalTitle(), Field.Store.NO));
                }
                doc.add(new TextField("title", m.getTitle(), Field.Store.NO));
                indexWriter.addDocument(doc);
            }

            numIndex = indexWriter.maxDoc();

            this.indexWriter.commit();

            this.status = true;
        } catch (IOException e) {

            this.ramDirectory = null;
            numIndex = -1;

            if (this.indexWriter != null && this.indexWriter.isOpen()) {
                try {
                    this.indexWriter.close();
                } catch (IOException ex) {
                    throw ex;
                } finally {
                    this.indexWriter = null;
                }
            }

            throw e;
        }

        return numIndex;
    }

    @Lock(LockType.WRITE)
    public void addToIndex(Film film) throws IOException {

        try {
            if(this.indexWriter == null || !this.indexWriter.isOpen()) {
                if(this.analyzer == null) {
                    this.analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);
                }
                IndexWriterConfig indexconfig = new IndexWriterConfig(analyzer);
                indexconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

                if(this.ramDirectory == null) {
                    this.ramDirectory = new RAMDirectory();
                }

                this.indexWriter = new IndexWriter(ramDirectory, indexconfig);
            }

            Document doc = new Document();

            doc.add(new StoredField("film_id", film.getId()));
            if(film.getOriginalTitle() != null) {
                doc.add(new TextField("original_title", film.getOriginalTitle(), Field.Store.NO));
            }
            doc.add(new TextField("title", film.getTitle(), Field.Store.NO));
            indexWriter.addDocument(doc);

            indexWriter.commit();

        } catch (IOException e) {
            if (this.indexWriter != null && this.indexWriter.isOpen()) {
                try {
                    this.indexWriter.close();
                } catch (IOException ex) {
                    this.indexWriter = null;
                    throw ex;
                }
            }
            throw e;
        }

    }

    @Lock(LockType.READ)
    public List<Integer> searchIndex(String queryStr, int maxHits) throws IOException {

        if(status && ramDirectory != null) {
            if(indexSearcher == null) {
                if(indexReader == null) {
                    indexReader = DirectoryReader.open(ramDirectory);
                }
                indexSearcher = new IndexSearcher(indexReader);
            }
        }

        queryStr = queryStr.toLowerCase();

        if(analyzer == null) {
            analyzer = new StandardAnalyzer();
        }

        BooleanQuery.Builder qBuilder =  new BooleanQuery.Builder();
        String[] input = queryStr.split(" ");
        //List<String> peliculas = new ArrayList<String>();
        ArrayList<Integer> peliculas = new ArrayList<Integer>();

        if(input.length == 1) {

            Query qr2 = new TermQuery(new Term("original_title", queryStr));
            Query qr1 = new TermQuery(new Term("title", queryStr));

            qBuilder.add(new BooleanClause(qr1, BooleanClause.Occur.SHOULD));
            qBuilder.add(new BooleanClause(qr2, BooleanClause.Occur.SHOULD));
        } else if(input.length > 1) {

            MultiPhraseQuery.Builder mpq1 = new MultiPhraseQuery.Builder();
            MultiPhraseQuery.Builder mpq2 = new MultiPhraseQuery.Builder();
            for (String string : input) {
                mpq1.add(new Term("original_title", string));
                mpq2.add(new Term("title", string));
            }
            MultiPhraseQuery mpquery1 = mpq1.build();
            MultiPhraseQuery mpquery2 = mpq2.build();
            qBuilder.add(new BooleanClause(mpquery1, BooleanClause.Occur.SHOULD));
            qBuilder.add(new BooleanClause(mpquery2, BooleanClause.Occur.SHOULD));
        }

        BooleanQuery queryNombres = qBuilder.build();
        TopDocs topDocs = indexSearcher.search(queryNombres, maxHits);
        ScoreDoc[] hits = topDocs.scoreDocs;

        //Ciclo para recorrer documentos entregados y guardarlos en una lista de strings
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document d = indexSearcher.doc(docId);
            peliculas.add(Integer.parseInt(d.get("film_id")));
        }

        return peliculas;
    }


}
