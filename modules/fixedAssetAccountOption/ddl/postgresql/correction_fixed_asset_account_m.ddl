-- Table: public.correction_fixed_asset_account_m

-- DROP TABLE IF EXISTS public.correction_fixed_asset_account_m;

CREATE TABLE IF NOT EXISTS public.correction_fixed_asset_account_m
(
    fixed_asset_account_id smallint NOT NULL,
    before_correction_string character varying(64) COLLATE pg_catalog."default" NOT NULL,
    after_correction_string character varying(64) COLLATE pg_catalog."default",
    insertdatetime timestamp without time zone,
    updatedatetime timestamp without time zone,
    updateuser character varying(32) COLLATE pg_catalog."default",
    CONSTRAINT correction_fixed_asset_account_m_pkey1 PRIMARY KEY (fixed_asset_account_id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.correction_fixed_asset_account_m
    OWNER to postgres;
-- Index: airead_correction_fixed_asset_account_m_before_correction_strin

-- DROP INDEX IF EXISTS public.airead_correction_fixed_asset_account_m_before_correction_strin;

CREATE INDEX IF NOT EXISTS airead_correction_fixed_asset_account_m_before_correction_strin
    ON public.correction_fixed_asset_account_m USING btree
    (before_correction_string COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
-- Index: airead_correction_fixed_asset_account_m_fixed_asset_account_id

-- DROP INDEX IF EXISTS public.airead_correction_fixed_asset_account_m_fixed_asset_account_id;

CREATE INDEX IF NOT EXISTS airead_correction_fixed_asset_account_m_fixed_asset_account_id
    ON public.correction_fixed_asset_account_m USING btree
    (fixed_asset_account_id ASC NULLS LAST)
    TABLESPACE pg_default;
