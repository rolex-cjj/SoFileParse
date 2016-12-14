package com.demo.parseso;


import com.demo.parseso.ElfType32.*;
import com.demo.parseso.ElfType64.*;
import com.demo.parseso.Utils;
import com.demo.parseso.EncodeSection;
import com.demo.parseso.ParseSo;

public class Test {
	
	public static ElfType32 type_32 = new ElfType32();
	public static ElfType64 type_64 = new ElfType64();
	public static String direc = "so-decrypt/arm64-v8a/";
	public static String srcfile = direc + "libMyJni.so";
	public static String desfile = direc + "libMyJni_.so";
	
	public static void main(String[] args){
		
		byte[] fileByteArys = Utils.readFile(srcfile);
		
		if(fileByteArys == null){
			System.out.println("read file byte failed...");
			return;
		}
		
		/**
		 * 先解析so文件
		 * 然后初始化AddSection中的一些信息
		 * 最后在AddSection
		 */
		byte ei_class = fileByteArys[4];
		if(ei_class == 1){
			System.out.println("32位目标");
			ParseSo.parseSo32(fileByteArys);
			EncodeSection.encodeSection32(fileByteArys);
			ParseSo.parseSo32(fileByteArys);
		}
		else if(ei_class == 2){
			System.out.println("64位目标");
			ParseSo.parseSo64(fileByteArys);
			EncodeSection.encodeSection64(fileByteArys);
			ParseSo.parseSo64(fileByteArys);
		}
		
		Utils.saveFile(desfile, fileByteArys);
		
		
		
		
//		//初始化AddSection中的变量
//		AddSection.sectionHeaderOffset = Utils.byte2Int(type_32.hdr.e_shoff);
//		AddSection.stringSectionInSectionTableIndex = Utils.byte2Short(type_32.hdr.e_shstrndx);
//		AddSection.stringSectionOffset = Utils.byte2Int(type_32.shdrList.get(AddSection.stringSectionInSectionTableIndex).sh_offset);
//		//找到第一个和最后一个类型为Load的Program header的index
//		boolean flag = true;
//		for(int i=0;i<type_32.phdrList.size();i++){
//			if(Utils.byte2Int(type_32.phdrList.get(i).p_type) == 1){//LOAD的type==1,可在elf格式文档中找到
//				if(flag){
//					AddSection.firstLoadInPHIndex = i;
//					flag = false;
//				}else{
//					AddSection.lastLoadInPHIndex = i;
//				}
//			}
//		}
//		int lastLoadVaddr = Utils.byte2Int(type_32.phdrList.get(AddSection.lastLoadInPHIndex).p_vaddr);
//		int lastLoadMem = Utils.byte2Int(type_32.phdrList.get(AddSection.lastLoadInPHIndex).p_memsz);
//		int lastLoadAlign = Utils.byte2Int(type_32.phdrList.get(AddSection.lastLoadInPHIndex).p_align);
//		AddSection.addSectionStartAddr = Utils.align(lastLoadVaddr + lastLoadMem, lastLoadAlign);
//		System.out.println("start addr hex:"+Utils.bytes2HexString(Utils.int2Byte(AddSection.addSectionStartAddr)));
//		
//		/**
//		 * 添加一个Section
//		 * 以下的顺序不可乱，不然会出错的
//		 * 1、添加一个Section Header
//		 * 2、直接在文件的末尾追加一个section
//		 * 3、修改String段的长度
//		 * 4、修改Elf Header中的section count
//		 */
//		fileByteArys = AddSection.addSectionHeader(fileByteArys);
//		fileByteArys = AddSection.addNewSectionForFileEnd(fileByteArys);
//		fileByteArys = AddSection.changeStrtabLen(fileByteArys);
//		fileByteArys = AddSection.changeElfHeaderSectionCount(fileByteArys);
//		fileByteArys = AddSection.changeProgramHeaderLoadInfo(fileByteArys);
		
	}
}
