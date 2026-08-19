-- Table: public.fixed_asset_account_m

-- DROP TABLE IF EXISTS public.fixed_asset_account_m;

CREATE TABLE IF NOT EXISTS public.fixed_asset_account_m
(
    fixed_asset_account_code integer NOT NULL,
    fixed_asset_account_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    fixed_asset_account_name_view character varying(255) COLLATE pg_catalog."default",
    insertdatetime date,
    updatedatetime date,
    updateuser character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT fixed_asset_account_m_pkey PRIMARY KEY (fixed_asset_account_code)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.fixed_asset_account_m
    OWNER to postgres;
