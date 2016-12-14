package com.demo.parseso;

import com.demo.parseso.ElfType32.*;
import com.demo.parseso.ElfType64.*;
import com.demo.parseso.Utils;

public class EncodeSection {

	public static String encodeSectionName = "mytext";

	/**
	 * 32加密
	 * 
	 * @param fileByteArys
	 */

	protected static void encodeSection32(byte[] fileByteArys) {
		// 读取String Section段
		System.out.println();

		int string_section_index = Utils.byte2Short(Test.type_32.hdr.e_shstrndx);
		elf32_shdr shdr = Test.type_32.shdrList.get(string_section_index);
		int size = Utils.byte2Int(shdr.sh_size);
		int offset = Utils.byte2Int(shdr.sh_offset);

		int mySectionOffset = 0, mySectionSize = 0;
		boolean bool = false;
		for (elf32_shdr temp : Test.type_32.shdrList) {
			int sectionNameOffset = offset + Utils.byte2Int(temp.sh_name);
			if (Utils.isEqualByteAry(fileByteArys, sectionNameOffset, encodeSectionName)) {
				// 这里需要读取section段然后进行数据加密
				mySectionOffset = Utils.byte2Int(temp.sh_offset);
				mySectionSize = Utils.byte2Int(temp.sh_size);
				byte[] sectionAry = Utils.copyBytes(fileByteArys, mySectionOffset, mySectionSize);
				for (int i = 0; i < sectionAry.length; i++) {
					sectionAry[i] = (byte) (sectionAry[i] ^ 0xFF);
				}
				Utils.replaceByteAry(fileByteArys, mySectionOffset, sectionAry);
				bool = true;
			}
		}
		if (!bool) {
			System.out.println("not found section");
		}
		int page_size = 4096;
		// 修改Elf Header中的entry和offset值
// int num_page = mySectionSize/page_size + (mySectionSize%page_size == 0 ? 0 : 1);
		byte[] entry = new byte[4];
		// entry = Utils.int2Byte((mySectionSize<<16) + num_page);
		entry = Utils.int2Byte(mySectionSize);
		Utils.replaceByteAry(fileByteArys, 24, entry);
		byte[] offsetAry = new byte[4];
		offsetAry = Utils.int2Byte(mySectionOffset);
		Utils.replaceByteAry(fileByteArys, 32, offsetAry);
		// System.out.println("num_page "+num_page);
		System.out.println("mySectionSize " + mySectionSize);
		System.out.println("entry " + Utils.bytes2HexString(entry));
		System.out.println("mySectionOffset " + mySectionOffset);
		System.out.println("offset " + Utils.bytes2HexString(offsetAry));

	}

	/**
	 * 64加密
	 * 
	 * @param fileByteArys
	 */
	protected static void encodeSection64(byte[] fileByteArys) {
		// 读取String Section段
		System.out.println();

		int string_section_index = Utils.byte2Short(Test.type_64.hdr.e_shstrndx);
		elf64_shdr shdr = Test.type_64.shdrList.get(string_section_index);
		int size = Utils.byte2Int(shdr.sh_size);
		int offset = Utils.byte2Int(shdr.sh_offset);

		int mySectionOffset = 0, mySectionSize = 0;
		boolean bool = false;
		for (elf64_shdr temp : Test.type_64.shdrList) {
			int sectionNameOffset = offset + Utils.byte2Int(temp.sh_name);
			if (Utils.isEqualByteAry(fileByteArys, sectionNameOffset, encodeSectionName)) {
				// 这里需要读取section段然后进行数据加密
				mySectionOffset = Utils.byte2Int(temp.sh_offset);
				mySectionSize = Utils.byte2Int(temp.sh_size);
				byte[] sectionAry = Utils.copyBytes(fileByteArys, mySectionOffset, mySectionSize);
				for (int i = 0; i < sectionAry.length; i++) {
					sectionAry[i] = (byte) (sectionAry[i] ^ 0xFF);
				}
				Utils.replaceByteAry(fileByteArys, mySectionOffset, sectionAry);
				bool = true;
			}
		}
		if (!bool) {
			System.out.println("not found section");
		}

		// 修改Elf Header中的entry和offset值
		int page_size = 4096;
		// char block_size = 16;
//		int num_page = mySectionSize / page_size + (mySectionSize % page_size == 0 ? 0 : 1);// section占用的页数
		byte[] entry = new byte[8];
		/**
		 * 将shdr -> addr 写入ELF头e_shoff，将shdr -> sh_size 和 addr
		 * 所在内存块写入e_entry中，即ehdr.e_entry = (length << 16) + nsize
		 */

//		entry = Utils.int2Byte((mySectionSize << 16) + num_page);
		entry = Utils.int2Byte(mySectionSize);
		Utils.replaceByteAry(fileByteArys, 24, entry);
		byte[] offsetAry = new byte[8];
		offsetAry = Utils.int2Byte(mySectionOffset);
		Utils.replaceByteAry(fileByteArys, 40, offsetAry);
//		System.out.println("num_page " + num_page);
		System.out.println("mySectionSize " + mySectionSize);
		System.out.println("entry " + Utils.bytes2HexString(entry));
		System.out.println("mySectionOffset " + mySectionOffset);
		System.out.println("offset " + Utils.bytes2HexString(offsetAry));
	}

}
