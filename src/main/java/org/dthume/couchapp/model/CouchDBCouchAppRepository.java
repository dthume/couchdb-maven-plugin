package org.dthume.couchapp.model;

import java.util.Collection;

import org.jcouchdb.db.Database;
import org.jcouchdb.document.DesignDocument;

public class CouchDBCouchAppRepository
	implements CouchAppRepository {

	private Database database;
	
	public CouchDBCouchAppRepository(final Database database) {
		this.database = database;
	}
	
	public Collection<String> listIds() {
		return java.util.Collections.emptyList();
	}

	@Override
	public DesignDocument create(DesignDocument app) {
		return update(app);
	}

	@Override
	public DesignDocument retrieve(String id) {
		return database.getDesignDocument(id);
	}

	@Override
	public DesignDocument update(DesignDocument app) {
		database.createOrUpdateDocument(app);
		return database.getDesignDocument(CouchAppConstants.toId(app));
	}

	@Override
	public boolean delete(DesignDocument app) {
		database.delete(app);
		return true;
	}
}
