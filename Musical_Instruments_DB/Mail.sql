CREATE TABLE mailcredentials (
	pkid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	email VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL
)

INSERT INTO mailcredentials (email, password) VALUES ('KyXHUk7goToK/mxCH5Zsjya7guY2Tl5p+kMWPh59YZzCDdQyJS5S1zzopoPW+lx+', 'q+nCQCUE0v+rvjo10XIL6q6g0+LKT8DOF4Yx8o2y64ShQ0sPRnQLF8vfGqI1Bxye');

DROP TABLE mailcredentials;