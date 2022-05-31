package com.kuiprux.animalcrossingbgmbot;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GoogleCloudHandler {

	public static String PROJECT_ID;
	public static String BUCKET_NAME;

	private static Storage storage;

	public static void init(String projectId, String bucketName/*, String credPath*/) {
		PROJECT_ID = projectId;
		BUCKET_NAME = bucketName;
		
		storage = StorageOptions.getDefaultInstance().getService();
	}
	
	public static String getActualName(String name) {
		Iterable<Blob> blobs = storage
				.list(BUCKET_NAME, Storage.BlobListOption.prefix(name), Storage.BlobListOption.currentDirectory())
				.getValues();

		for (Blob blob : blobs) {
			return blob.getName();
		}
		return null;
	}
	
	public static List<String> getActualNames(String name) {
		Iterable<Blob> blobs = storage
				.list(BUCKET_NAME, Storage.BlobListOption.prefix(name), Storage.BlobListOption.currentDirectory())
				.getValues();

		List<String> names = new ArrayList<>();
		for (Blob blob : blobs) {
			names.add(blob.getName());
		}
		return names;
	}
/*
	public static byte[] getBgmFile(String titleName, String weatherName, int currentHour) {
		return getFileWithPrefix(getBgmFileName(titleName, weatherName, currentHour));
	}
*/
	public static String getBgmFileName(String titleName, String weatherName, int currentHour) {
		return titleName + "/" + weatherName + "/" + weatherName + "-" + String.format("%02d", currentHour);
	}
/*
	public static byte[] getFileWithPrefix(String name) {
		System.out.println(name);
		Iterable<Blob> blobs = storage
				.list(BUCKET_NAME, Storage.BlobListOption.prefix(name), Storage.BlobListOption.currentDirectory())
				.getValues();

		for (Blob blob : blobs) {
			System.out.println(blob.getBlobId());
			return storage.readAllBytes(blob.getBlobId());
		}

		return null;
	}
*/
	public static byte[] getFileWithName(String name) {
		System.out.println(name);
		return storage.readAllBytes(storage.get(BlobId.of(BUCKET_NAME, name)).getBlobId());
	}
}