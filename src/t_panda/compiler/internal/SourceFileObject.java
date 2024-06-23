package t_panda.compiler.internal;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class SourceFileObject extends SimpleJavaFileObject {
    private final String code;

    public SourceFileObject(String fqcn, String code) {
		super(URI.create("string:///" + fqcn.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
    }
    
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        if(ignoreEncodingErrors) {
            return this.code;
        }
        //TODO: エンコーディングエラー無視しないモード
        return this.code;
    }
}
