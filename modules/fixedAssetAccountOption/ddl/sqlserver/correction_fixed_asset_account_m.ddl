-- Table: public.correction_fixed_asset_account_m

-- DROP TABLE IF EXISTS public.correction_fixed_asset_account_m;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[correction_fixed_asset_account_m]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[correction_fixed_asset_account_m] (
    [fixed_asset_account_id] smallint NOT NULL,
    [before_correction_string] nvarchar(64) NOT NULL,
    [after_correction_string] nvarchar(64),
    [insertdatetime] datetime,
    [updatedatetime] datetime,
    [updateuser] nvarchar(32),
    CONSTRAINT [correction_fixed_asset_account_m_pkey1] PRIMARY KEY CLUSTERED ([fixed_asset_account_id] ASC)
    );
END
GO

-- index: before_correction_string
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = N'airead_correction_fixed_asset_account_m_before_correction_strin' AND object_id = OBJECT_ID(N'[dbo].[correction_fixed_asset_account_m]'))
BEGIN
CREATE INDEX [airead_correction_fixed_asset_account_m_before_correction_strin]
    ON [dbo].[correction_fixed_asset_account_m] ([before_correction_string] ASC);
END
GO

-- index: fixed_asset_account_id
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = N'airead_correction_fixed_asset_account_m_fixed_asset_account_id' AND object_id = OBJECT_ID(N'[dbo].[correction_fixed_asset_account_m]'))
BEGIN
CREATE INDEX [airead_correction_fixed_asset_account_m_fixed_asset_account_id]
    ON [dbo].[correction_fixed_asset_account_m] ([fixed_asset_account_id] ASC);
END
GO
