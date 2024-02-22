package own.watcharapon.repository;

import own.watcharapon.payload.IndustryPlainPayload;

import java.util.List;

public interface IndustryRepository {
    List<IndustryPlainPayload> getAll();
}
