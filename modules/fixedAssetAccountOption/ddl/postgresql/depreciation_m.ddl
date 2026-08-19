-- Table: public.depreciation_m

-- DROP TABLE IF EXISTS public.depreciation_m;

CREATE TABLE IF NOT EXISTS public.depreciation_m
(
    depreciation_code integer NOT NULL,
    depreciation_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    depreciation_name_view character varying(255) COLLATE pg_catalog."default",
    insertdatetime date,
    updatedatetime date,
    updateuser character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT depreciation_m_pkey PRIMARY KEY (depreciation_code)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.depreciation_m
    OWNER to postgres;
