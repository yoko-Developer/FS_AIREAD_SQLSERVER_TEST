-- Table: public.correction_depreciation_method_m

-- DROP TABLE IF EXISTS public.correction_depreciation_method_m;

CREATE TABLE IF NOT EXISTS public.correction_depreciation_method_m
(
    correction_depreciation_method_id smallint NOT NULL,
    before_correction_string character varying(64) COLLATE pg_catalog."default" NOT NULL,
    after_correction_string character varying(64) COLLATE pg_catalog."default",
    insertdatetime timestamp without time zone,
    updatedatetime timestamp without time zone,
    updateuser character varying(32) COLLATE pg_catalog."default",
    CONSTRAINT correction_depreciation_method_m_pkey1 PRIMARY KEY (correction_depreciation_method_id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.correction_depreciation_method_m
    OWNER to postgres;
-- Index: airead_correction_depreciation_method_m_before_correction_strin

-- DROP INDEX IF EXISTS public.airead_correction_depreciation_method_m_before_correction_strin;

CREATE INDEX IF NOT EXISTS airead_correction_depreciation_method_m_before_correction_strin
    ON public.correction_depreciation_method_m USING btree
    (before_correction_string COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
-- Index: airead_correction_depreciation_method_m_correction_depreciation

-- DROP INDEX IF EXISTS public.airead_correction_depreciation_method_m_correction_depreciation;

CREATE INDEX IF NOT EXISTS airead_correction_depreciation_method_m_correction_depreciation
    ON public.correction_depreciation_method_m USING btree
    (correction_depreciation_method_id ASC NULLS LAST)
    TABLESPACE pg_default;
