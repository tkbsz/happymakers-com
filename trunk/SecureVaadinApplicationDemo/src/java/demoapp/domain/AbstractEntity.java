package demoapp.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
	
	@Id
	private String uuid;
	@Version
	private Long version;

	public AbstractEntity() {
		uuid = UUID.randomUUID().toString();
	}

	public UUID getUUID() {
		return UUID.fromString(uuid);
	}

	public String getUUIDAsString() {
		return uuid;
	}

	public Long getVersion() {
		return version;
	}
}
