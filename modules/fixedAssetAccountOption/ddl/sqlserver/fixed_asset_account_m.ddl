-- Table: public.fixed_asset_account_m

-- DROP TABLE IF EXISTS public.fixed_asset_account_m;

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[fixed_asset_account_m]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[fixed_asset_account_m] (
    [fixed_asset_account_code] integer NOT NULL,
    [fixed_asset_account_name] nvarchar(255) NOT NULL,
    [fixed_asset_account_name_view] nvarchar(255),
    [insertdatetime] date,
    [updatedatetime] date,
    [updateuser] nvarchar(255),
    CONSTRAINT [fixed_asset_account_m_pkey] PRIMARY KEY CLUSTERED ([fixed_asset_account_code] ASC)
    );
END
GO
