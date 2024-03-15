CREATE TABLE mailcredentials (
	pkid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	email VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL
)

INSERT INTO mailcredentials (email, password) VALUES ('KyXHUk7goToK/mxCH5Zsjya7guY2Tl5p+kMWPh59YZzCDdQyJS5S1zzopoPW+lx+', 'T3pwMKi0fVPOMbDV4x4CpA==');

DROP TABLE mailcredentials;