-- 1. テーブルの作成
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[correction_financial_institution_m]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[correction_financial_institution_m] (
        [correction_financial_institution_id] INT NOT NULL,
        [fluctuation_string]                  NVARCHAR(255),
        [correction_string]                   NVARCHAR(255),
        [insertdatetime]                      DATETIME,
        [updatedatetime]                      DATETIME,
        [updateuser]                          NVARCHAR(255),
        CONSTRAINT [correction_financial_institution_m_pkey] PRIMARY KEY CLUSTERED ([correction_financial_institution_id] ASC)
    );
END
GO

-- 2. インデックスの作成
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = N'idx_correction_fi_fluctuation' AND object_id = OBJECT_ID(N'[dbo].[correction_financial_institution_m]'))
BEGIN
    CREATE INDEX [idx_correction_fi_fluctuation] 
    ON [dbo].[correction_financial_institution_m] ([fluctuation_string] ASC);
END
GO