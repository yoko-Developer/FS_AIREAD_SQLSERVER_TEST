######################################################################
### 
### スクリプト名 : CallSetFixedAssetAccountCodeBatch.ps1
### 引数　　　　 : 第一引数: CSVファイルパス
### 機能概要　　 : 固定資産科目コード設定バッチ処理を実行する
### 
######################################################################

######################################################################
### 関数定義
######################################################################

# Log4netの初期化
# return - なし
function initLog4net($parentDir, $appenderName) {
    $log4netDllRelativePath = $parentDir + "\..\lib\log4net.dll";
    $log4netDllPath = (Resolve-Path -Path $log4netDllRelativePath | Get-ChildItem ).Fullname;
    if (-!(Test-Path -Path $log4netDllPath -PathType Leaf)) {
        Write-Host "Log4netのDLLが存在しません。DLLファイル=" + $log4netDllPath
        throw;
    }
    $dllBytes = [System.IO.File]::ReadAllBytes( $log4netDllPath );
    [System.Reflection.Assembly]::Load($dllBytes);

    $log4netConfRelativePath = $parentDir + "\..\conf\log4net_" + $appenderName + ".xml";
    $log4netConfPath = (Resolve-Path -Path $log4netConfRelativePath | Get-ChildItem ).Fullname;
    if (-!(Test-Path -Path $log4netConfPath -PathType Leaf)) {
        Write-Host "Log4netの設定ファイルが存在しません。設定ファイル=" + $log4netConfPath
        throw;
    }
    $log4netConf = Get-Item $log4netConfPath;
    [log4net.Config.XmlConfigurator]::Configure($log4netConf);
    $global:logger = [log4net.LogManager]::GetLogger($appenderName);
}

# 設定ファイルのエラーチェック
# return - なし
function checkConf() {
    $global:logger.info("設定ファイルのチェックを開始");

    if ([String]::IsNullOrEmpty($global:appConf.JAR_PATH)) {
        $global:logger.error("JARファイルパスが指定されていません。 Key=JAR_PATH");
        throw;
    }
    
    if (-![System.IO.Path]::IsPathRooted($global:appConf.JAR_PATH)) {
        $jarPathFull = Join-Path $baseDir $global:appConf.JAR_PATH;
        $global:appConf.JAR_PATH = [System.IO.Path]::GetFullPath($jarPathFull);
    }
    
    if (-!(Test-Path -Path $global:appConf.JAR_PATH -PathType Leaf)) {
        $global:logger.error("JARファイルが存在しません。 JARファイル=" + $global:appConf.JAR_PATH);
        throw;
    }
    
    if ([String]::IsNullOrEmpty($global:appConf.CONFIG_PATH)) {
        $global:logger.error("設定ファイルパスが指定されていません。 Key=CONFIG_PATH");
        throw;
    }
    
    if (-![System.IO.Path]::IsPathRooted($global:appConf.CONFIG_PATH)) {
        $configPathFull = Join-Path $baseDir $global:appConf.CONFIG_PATH;
        $global:appConf.CONFIG_PATH = [System.IO.Path]::GetFullPath($configPathFull);
    }
    
    if (-!(Test-Path -Path $global:appConf.CONFIG_PATH -PathType Leaf)) {
        $global:logger.error("設定ファイルが存在しません。 設定ファイル=" + $global:appConf.CONFIG_PATH);
        throw;
    }

    # Java実行ファイルのチェック
    if ([String]::IsNullOrEmpty($global:appConf.JAVA_PATH)) {
        $global:logger.error("Javaファイルパスが指定されていません。 Key=JAVA_PATH");
        throw;
    }

    if (-!(Test-Path -Path $global:appConf.JAVA_PATH -PathType Leaf)) {
        $global:logger.error("Javaが存在しません。 Javaパス=" + $global:appConf.JAVA_PATH);
        throw;
    }
    
    $global:logger.info("設定ファイルのチェックを完了");
}

######################################################################
### メイン処理
######################################################################
$global:logger = $null;
$global:appConf = $null;

$baseDir = Convert-Path $(Split-Path $MyInvocation.InvocationName -Parent).Trim()
$psName = Split-Path $MyInvocation.InvocationName -Leaf
$psBaseName = $psName -replace "\.ps1$", ""

if ($args.Length -lt 1 ) {
    Write-Host "[Error] 引数の指定が足りません";
    Write-Host "[Error] " + $psBaseName + "をエラー終了";
    return 1;
}
$csvFilePath = $args[0];

# AIReadから渡されたCSVファイルパスから、
# csv_AIReadフォルダの親フォルダを取得する
$csvDirPath = Split-Path -Path $csvFilePath -Parent
$inputDirPath = Split-Path -Path $csvDirPath -Parent

$confFilePath = "\..\conf\{0}.ini" -f  $psBaseName
$confFilePath = $baseDir + $confFilePath;

initLog4net $baseDir $psBaseName

$global:logger.info($psBaseName + "を起動");
$global:logger.info("[引数1] " + $csvFilePath);
$global:logger.info("[設定ファイル] " + $confFilePath);

if ([String]::IsNullOrEmpty($confFilePath) -Or -!(Test-Path -Path $confFilePath -PathType leaf)) {
    $global:logger.error("設定ファイルが存在しません。 設定ファイル=" + $confFilePath);
    throw;
}

$global:appConf = @{};
Get-Content $confFilePath | %{ $global:appConf += ConvertFrom-StringData $_ };

checkConf;
$exitCode=9;

if (-!(Test-Path -Path $csvFilePath -PathType Leaf)) {
    $global:logger.error("CSVファイルが存在しません。 CSVファイル=" + $csvFilePath);
    throw;
}

$global:logger.info("CSVファイル=" + $csvFilePath);
$global:logger.info("入力フォルダ=" + $inputDirPath);
$global:logger.info("JARファイル=" + $global:appConf.JAR_PATH);

for ($execCnt = 0; $execCnt -lt $global:appConf.RETRY_COUNT; $execCnt++) {
    try {
        $global:logger.info("固定資産科目コード設定バッチを実行(" + ($execCnt + 1) + "回目)");
        
        $javaPath = $global:appConf.JAVA_PATH;

        $springConfigLocation = "--spring.config.location=file:///" + $global:appConf.CONFIG_PATH.Replace("\", "/");
        $javaCommand = "`"$javaPath`" -jar `"$($global:appConf.JAR_PATH)`" $springConfigLocation `"$inputDirPath`"";
        $global:logger.info("実行コマンド: " + $javaCommand);

        $result = & $javaPath -jar $global:appConf.JAR_PATH $springConfigLocation $inputDirPath;

        if ($LASTEXITCODE -eq 0) {
            $global:logger.info("固定資産科目コード設定バッチが正常終了しました");
            $exitCode = 0;
            break;
        } else {
            $global:logger.warn("固定資産科目コード設定バッチがエラー終了しました。終了コード=" + $LASTEXITCODE);
            if ($execCnt -lt ($global:appConf.RETRY_COUNT - 1)) {
                $global:logger.info("リトライします。待機時間=" + $global:appConf.SLEEP_SECONDS + "秒");
                Start-Sleep -Seconds $global:appConf.SLEEP_SECONDS;
            }
        }
    } catch {
        $global:logger.error("例外が発生しました: " + $error[0]);
        if ($execCnt -lt ($global:appConf.RETRY_COUNT - 1)) {
            $global:logger.info("リトライします。待機時間=" + $global:appConf.SLEEP_SECONDS + "秒");
            Start-Sleep -Seconds $global:appConf.SLEEP_SECONDS;
        }
    }
}

if ($exitCode -eq 0) {
    $global:logger.info($psBaseName + "を正常終了");
} else {
    $global:logger.error($psBaseName + "をエラー終了");
}

# 自動的に終了（Enterキーを待たない）
[Environment]::Exit($exitCode);
