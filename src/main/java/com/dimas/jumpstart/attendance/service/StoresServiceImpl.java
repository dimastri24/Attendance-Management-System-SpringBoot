package com.dimas.jumpstart.attendance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dimas.jumpstart.attendance.dao.Stores;
import com.dimas.jumpstart.attendance.exception.BadRequestException;
import com.dimas.jumpstart.attendance.exception.ResourceNotFoundException;
import com.dimas.jumpstart.attendance.repo.StoresRepo;

@Service
@Transactional
public class StoresServiceImpl implements StoresService{
	
	@Autowired
	private StoresRepo storesRepo;

	@Override
	public List<Stores> viewAllStores() {
		return storesRepo.findAll();
	}

	@Override
	public void addStore(Stores store) {
		storesRepo.save(store);
	}

	@Override
	public Stores getById(long id) {
		return storesRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Stores", "storeId", id));
	}

	@Override
	public void deleteById(long id) {
		storesRepo.deleteById(id);
	}

//	@Override
//	public List<Stores> getStoresByKey(String key) {
//		return storesRepo.searchBykey(key);
//	}

	@Override
	public void updateStore(long id, Stores store) {
		Stores edit = storesRepo.findById(id).get();
		edit.setStoreOwner(store.getStoreOwner());
		edit.setContactPhone(store.getContactPhone());
		edit.setStoreAddress(store.getStoreAddress());
		edit.setLocationCountry(store.getLocationCountry());
		edit.setLocationCity(store.getLocationCity());
		edit.setClockEntry(store.getClockEntry());
		edit.setClockOut(store.getClockOut());
		storesRepo.save(edit);
	}
	
//	public Stores getStore(long storeId) {
//		return storesRepo.findById(storeId)
//				.orElseThrow(() -> new BadRequestException("Sorry. We couldn't find any related store")); 
//	}

}
