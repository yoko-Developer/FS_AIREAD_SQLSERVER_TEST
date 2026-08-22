@echo off

rem AIRead個別後処理から渡されるCSVファイルパス
set "CSV_FILE_PATH=%~1"

rem CSVが格納されている csv_AIRead フォルダを取得
for %%I in ("%CSV_FILE_PATH%") do set "CSV_DIR=%%~dpI"

rem csv_AIRead の親フォルダを取得
for %%I in ("%CSV_DIR%..") do set "WORK_DIR_ROOT=%%~fI"

rem JARファイルと外部設定ファイル
set "JAR_PATH=%~dp0..\lib\setfixedassetaccountcode-0.0.1-SNAPSHOT.jar"
set "CONFIG_PATH=%~dp0..\conf\application.yaml"

rem 固定資産科目・償却方法区分紐付け処理
java -jar "%JAR_PATH%" ^
  "--spring.config.location=file:%CONFIG_PATH%" ^
  "%WORK_DIR_ROOT%"

exit /b %errorlevel%
