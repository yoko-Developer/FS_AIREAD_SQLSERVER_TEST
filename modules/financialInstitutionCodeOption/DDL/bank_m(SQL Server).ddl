-- テーブル作成 (public. は削除し、SQL Server標準の dbo. に変更)
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[bank_m]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[bank_m] (
        [bank_code] nvarchar(255) NOT NULL,
        [branch_no] smallint NOT NULL,
        [bank_name] nvarchar(255),
        [bank_name_view] nvarchar(255),
        [insertdatetime] datetime,
        [updatedatetime] datetime,
        [updateuser] nvarchar(255),
        CONSTRAINT [bank_m_pkey] PRIMARY KEY ([bank_code], [branch_no])
    );
END
GO

-- インデックス作成
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'jfcidx_bank_m_bank_code' AND object_id = OBJECT_ID('dbo.bank_m'))
BEGIN
    CREATE INDEX [jfcidx_bank_m_bank_code]
    ON [dbo].[bank_m] ([bank_code] ASC);
END
GO