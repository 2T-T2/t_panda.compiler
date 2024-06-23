package t_panda.compiler.internal;

import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class ClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {
	private final Map<String, ClassFileObject> map;

    public ClassFileManager(JavaCompiler compiler, DiagnosticListener<? super JavaFileObject> listener) {
		super(compiler.getStandardFileManager(listener, null, null));
        this.map = new HashMap<String, ClassFileObject>();
	}

    @Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
		ClassFileObject co = new ClassFileObject(className, kind);
		map.put(className, co);
		return co;
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
		return new SecureClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                ClassFileObject co = map.get(name);
                if (co == null) {
                    return super.findClass(name);
                }

                if (!co.getDefinedClass().isPresent()) {
                    byte[] b = co.getCompiledBytes();
                    co.setDefinedClass(super.defineClass(name, b, 0, b.length));
                }
                return co.getDefinedClass().get();
            }
        };
	}
}
