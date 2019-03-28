package org.graphql.maven.plugin.samples.server.generated;

import javax.persistence.Id;

/**
 * @author generated by graphql-maven-plugin
 */
// @Entity
public enum Episode {
	NEWHOPE(1), EMPIRE(2), JEDI(3);

	@Id
	private int id;

	Episode(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
