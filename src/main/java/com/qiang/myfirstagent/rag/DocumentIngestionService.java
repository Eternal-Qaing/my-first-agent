package com.qiang.myfirstagent.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author qiang
 * @since 2026/10/15 18:55
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentIngestionService {
    private final VectorStore vectorStore;

    public int ingest(Resource resource){
        log.info("开始入库文档 | filename={}", resource.getFilename());
        //1.读取文档
        TextReader reader = new TextReader(resource);
        List<Document> documents = reader.get();

        //2.分块（默认 chunkSize=800 token, 重叠 200）
        TokenTextSplitter splitter = TokenTextSplitter.builder().build();
        List<Document> chunks = splitter.apply(documents);

        //3.写入向量库（自动调用 Embedding 模型转向量）
        vectorStore.add(chunks);
        log.info("文档入库完成 | filename={} | chunks={}", resource.getFilename(), chunks.size());
        return  chunks.size();
    }
}
