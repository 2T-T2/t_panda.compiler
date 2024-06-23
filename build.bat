setlocal enabledelayedexpansion

:start
    @echo off
    set error_msg=0
    set out_dir=.\out\
    set dst_dir=.\dst\
    set doc_dir=.\javadoc\
    echo =============== クリーン開始 ===============
    del /s /q %out_dir%
    del /s /q %dst_dir%
    del /s /q %doc_dir%
    rmdir /s /q %out_dir%
    rmdir /s /q %dst_dir%
    rmdir /s /q %doc_dir%
    mkdir %out_dir%
    mkdir %dst_dir%
    mkdir %doc_dir%
    echo =============== クリーン終了 ===============
    echo.
    echo =============== コンパイル開始 ===============
    set javacCmd=javac -sourcepath .\src\ -d %out_dir% .\src\t_panda\compiler\*.java
    echo %javacCmd%
    %javacCmd%
    if %errorlevel% neq 0 (
        set error_msg=コンパイルエラー
        goto :echo_error
    )
    echo =============== コンパイル終了 ===============
    echo.
    echo =============== アーカイブ化開始 ===============
    set jarCmd=jar -cf %dst_dir%t_panda.compiler.jar -C %out_dir% .
    echo %jarCmd%
    %jarCmd%
    if %errorlevel% neq 0 (
        set error_msg=アーカイブ化エラー
        goto :echo_error
    )
    echo =============== アーカイブ化開始 ===============
    echo.
    echo =============== ドキュメント生成開始 ===============
    set javadocCmd=javadoc ^
        --allow-script-in-comments ^
        -d %doc_dir% ^
        -sourcepath .\src\ ^
        -subpackages t_panda.compiler ^
        -exclude t_panda.compiler.internal ^
        -header "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.15.10/styles/vs.min.css'><script src='https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.15.10/highlight.min.js'></script><script>hljs.initHighlightingOnLoad();</script>"
    echo %javadocCmd%
    %javadocCmd%
    if %errorlevel% neq 0 (
        set error_msg=ドキュメント生成エラー
        goto :echo_error
    )
    echo =============== ドキュメント生成開始 ===============
goto :end

:echo_error
    echo %error_msg%
goto :end

:end
    ENDLOCAL