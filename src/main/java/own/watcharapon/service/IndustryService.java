package own.watcharapon.service;

import org.springframework.stereotype.Service;
import own.watcharapon.payload.IndustryPayload;
import own.watcharapon.payload.IndustryPlainPayload;
import own.watcharapon.repository.IndustryRepository;

import java.util.*;

@Service
public class IndustryService {
    private final IndustryRepository industryRepository;

    public IndustryService(IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }

    public List<IndustryPayload> getAllIndustry() {
        List<IndustryPlainPayload> industryPlainPayloadList = industryRepository.getAll();
        Map<UUID, IndustryPayload> industryPayloadMap = new LinkedHashMap<>(); // LinkedHashMap to maintain insertion order

        for (IndustryPlainPayload plainPayload : industryPlainPayloadList) {
            IndustryPayload industryPayload = industryPayloadMap.computeIfAbsent(plainPayload.getSectorId(), id -> {
                IndustryPayload payload = new IndustryPayload();
                payload.setId(plainPayload.getSectorId());
                payload.setSector(plainPayload.getSector());
                payload.setIndustryList(new ArrayList<>());
                return payload;
            });

            // Check if the sector is already set
            if (industryPayload.getSector() == null) {
                industryPayload.setSector(plainPayload.getSector());
            }

            IndustryPayload.Industry industry = new IndustryPayload.Industry();
            industry.setId(plainPayload.getIndustryId());
            industry.setIndustryName(plainPayload.getIndustry());

            // Add industry to the list only if it's not already present
            if (!containsIndustry(industryPayload.getIndustryList(), plainPayload.getIndustry())) {
                industryPayload.getIndustryList().add(industry);
            }
        }

        return new ArrayList<>(industryPayloadMap.values());
    }

    private boolean containsIndustry(List<IndustryPayload.Industry> industryList, String industryName) {
        for (IndustryPayload.Industry industry : industryList) {
            if (industry.getIndustryName().equals(industryName)) {
                return true;
            }
        }
        return false;
    }
}
