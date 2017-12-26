package com.saltware.enface.enboard.service.impl;

import java.util.Map;

import com.saltware.enface.enboard.dao.BulletinDao;
import com.saltware.enface.enboard.service.BulletinService;

public class BulletinServiceImpl implements BulletinService {
	private BulletinDao bulletinDao;
	
	public void setBulletinDao(BulletinDao bulletinDao) {
		this.bulletinDao = bulletinDao;
	}

	@Override
	public int selectAllCount(Map paramMap) throws Exception {
		return bulletinDao.selectAllCount(paramMap);
	}

	@Override
	public int selectVisionBoardUserCount(Map paramMap) throws Exception {
		return bulletinDao.selectVisionBoardUserCount(paramMap);
	}
}
