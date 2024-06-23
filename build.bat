setlocal enabledelayedexpansion

:start
    @echo off
    set error_msg=0
    set out_dir=.\out\
    set dst_dir=.\dst\
    set doc_dir=.\javadoc\
    echo =============== �N���[���J�n ===============
    del /s /q %out_dir%
    del /s /q %dst_dir%
    del /s /q %doc_dir%
    rmdir /s /q %out_dir%
    rmdir /s /q %dst_dir%
    rmdir /s /q %doc_dir%
    mkdir %out_dir%
    mkdir %dst_dir%
    mkdir %doc_dir%
    echo =============== �N���[���I�� ===============
    echo.
    echo =============== �R���p�C���J�n ===============
    set javacCmd=javac -sourcepath .\src\ -d %out_dir% .\src\t_panda\compiler\*.java
    echo %javacCmd%
    %javacCmd%
    if %errorlevel% neq 0 (
        set error_msg=�R���p�C���G���[
        goto :echo_error
    )
    echo =============== �R���p�C���I�� ===============
    echo.
    echo =============== �A�[�J�C�u���J�n ===============
    set jarCmd=jar -cf %dst_dir%t_panda.compiler.jar -C %out_dir% .
    echo %jarCmd%
    %jarCmd%
    if %errorlevel% neq 0 (
        set error_msg=�A�[�J�C�u���G���[
        goto :echo_error
    )
    echo =============== �A�[�J�C�u���J�n ===============
    echo.
    echo =============== �h�L�������g�����J�n ===============
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
        set error_msg=�h�L�������g�����G���[
        goto :echo_error
    )
    echo =============== �h�L�������g�����J�n ===============
goto :end

:echo_error
    echo %error_msg%
goto :end

:end
    ENDLOCAL