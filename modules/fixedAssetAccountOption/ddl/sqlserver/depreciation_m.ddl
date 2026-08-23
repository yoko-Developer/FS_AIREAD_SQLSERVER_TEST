-- Table: public.depreciation_m

-- DROP TABLE IF EXISTS public.depreciation_m;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[depreciation_m]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[depreciation_m] (
    [depreciation_code] integer NOT NULL,
    [depreciation_name] nvarchar(255) NOT NULL,
    [depreciation_name_view] nvarchar(255),
    [insertdatetime] date,
    [updatedatetime] date,
    [updateuser] nvarchar(255),
    CONSTRAINT [depreciation_m_pkey] PRIMARY KEY CLUSTERED ([depreciation_code] ASC)
    );
END
GO
