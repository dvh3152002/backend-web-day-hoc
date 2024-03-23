package com.example.backendkhoaluan.utils;

import javax.tools.SimpleJavaFileObject;

public class JavaSourceFromString extends SimpleJavaFileObject {
    final String code;

    public JavaSourceFromString(String name, String code) {
        super(java.net.URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
