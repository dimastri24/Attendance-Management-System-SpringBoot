package com.dimas.jumpstart.attendance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dimas.jumpstart.attendance.dao.Stores;

@Service
@Transactional
public interface StoresService {
	
	List<Stores> viewAllStores();
	void addStore(Stores store);
	Stores getById(long id);
	void deleteById(long id);
	//List<Stores> getStoresByKey(String key);
	void updateStore(long id, Stores store);
//	Stores getStore(long storeId);

}
