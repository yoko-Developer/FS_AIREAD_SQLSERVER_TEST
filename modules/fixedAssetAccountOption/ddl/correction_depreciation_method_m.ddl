-- Table: public.correction_depreciation_method_m

-- DROP TABLE IF EXISTS public.correction_depreciation_method_m

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[correction_depreciation_method_m]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[correction_depreciation_method_m] (
    [correction_depreciation_method_id] smallint NOT NULL,
    [before_correction_string] nvarchar(64) NOT NULL,
    [after_correction_string] nvarchar(64),
    [insertdatetime] datetime,
    [updatedatetime] datetime,
    [updateuser] nvarchar(32),
    CONSTRAINT [correction_depreciation_method_m_pkey1] PRIMARY KEY CLUSTERED ([correction_depreciation_method_id] ASC)
    );
END
GO

-- index: before_correction_string
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = N'airead_correction_depreciation_method_m_before_correction_strin' AND object_id = OBJECT_ID(N'[dbo].[correction_depreciation_method_m]'))
BEGIN
CREATE INDEX [airead_correction_depreciation_method_m_before_correction_strin]
    ON [dbo].[correction_depreciation_method_m] ([before_correction_string] ASC);
END
GO

-- index: correction_depreciation_method_id
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = N'airead_correction_depreciation_method_m_correction_depreciation' AND object_id = OBJECT_ID(N'[dbo].[correction_depreciation_method_m]'))
BEGIN
CREATE INDEX [airead_correction_depreciation_method_m_correction_depreciation]
    ON [dbo].[correction_depreciation_method_m] ([correction_depreciation_method_id] ASC);
END
GO
