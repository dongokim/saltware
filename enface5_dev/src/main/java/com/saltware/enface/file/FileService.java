package com.saltware.enface.file;

import java.util.List;

public interface FileService {

	public int save(FileVO paramVO);

	public List<FileVO> list();

}
