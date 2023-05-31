const _cmd = {
	"mysql": {
		"server": {
			"run": `
				cd "C:/Program Files/MySQL/MySQL 8.0.33"; bin/mysqld;
			`
		},
		"client": {
			"run": `
				cd "C:/Program Files/MySQL/MySQL 8.0.33"; bin/mysql -u root --local-infile=1;
			`
		}
	}
};
