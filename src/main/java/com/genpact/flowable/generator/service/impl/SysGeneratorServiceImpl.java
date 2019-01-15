package com.genpact.flowable.generator.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genpact.flowable.generator.dao.SysGeneratorDao;
import com.genpact.flowable.generator.service.SysGeneratorService;
import com.genpact.flowable.generator.util.GeneratorUtils;

/**
 * @author sxia
 * @Description: TODO()
 * @date 2017-6-23 15:07
 */
@Service("sysGeneratorService")
public class SysGeneratorServiceImpl implements SysGeneratorService {

	@Autowired
	private SysGeneratorDao sysGeneratorDao;

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		return sysGeneratorDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysGeneratorDao.queryTotal(map);
	}

	@Override
	public Map<String, String> queryTable(String tableName) {
		return sysGeneratorDao.queryTable(tableName);
	}

	@Override
	public List<Map<String, String>> queryColumns(String tableName) {
		return sysGeneratorDao.queryColumns(tableName);
	}

	@Override
	public byte[] generatorCode(String[] tableNames) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		for (String tableName : tableNames) {
			// 查询表信息
			Map<String, String> table = queryTable(tableName);
			// 查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			// 生成代码
			GeneratorUtils.generatorCode(table, columns, zip);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	@Override
	public void generatorCode(String zipPath, String[] tableNames) {
		FileOutputStream outputStream = null;
		try {
			File zipFile = new File(zipPath);
			if(!zipFile.exists() ) {
				zipFile.createNewFile();
			}
			outputStream = new FileOutputStream(zipFile);
			ZipOutputStream zip = new ZipOutputStream(outputStream);

			for (String tableName : tableNames) {
				Map<String, String> table = queryTable(tableName);
				List<Map<String, String>> columns = queryColumns(tableName);
				GeneratorUtils.generatorCode(table, columns, zip);
			}
			IOUtils.closeQuietly(zip);
		} catch ( IOException e) {
			e.printStackTrace();
		}
	}
}
