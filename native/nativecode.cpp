/**
 * 解密函数
 */
void init_mytext() __attribute__((constructor));
unsigned long getLibAddr();//获取so的起始地址             

void init_mytext (){

    unsigned int nblock;
    unsigned int num_page;//section占用的页数
    unsigned long base;
    unsigned long text_addr;
    unsigned int i;
    Elf32_Ehdr *ehdr32;
    Elf64_Ehdr *ehdr64;

    //获取so的起始地址
    base = getLibAddr();

    //获取指定section的偏移值和size
    ehdr32 = (Elf32_Ehdr *)base;
    int type = *((ehdr32->e_ident) + 4);
    __android_log_print(ANDROID_LOG_INFO, "JNITag", "type =  %d", type);
     if(type == 1){
         ehdr32 = (Elf32_Ehdr *)base;
         text_addr = ehdr32->e_shoff + base;
         nblock = ehdr32->e_entry;
//          nblock = ehdr32->e_entry >> 16;
//          num_page = ehdr32->e_entry & 0xffff;
     }
     else if(type == 2){
         ehdr64 = (Elf64_Ehdr *)base;
         text_addr = ehdr64->e_shoff + base;
         nblock = ehdr64->e_entry;
//          nblock = ehdr64->e_entry >> 16;
//          num_page = ehdr64->e_entry & 0xffff;
     }                                                                                                                                                                                                                                                                                                                              
    num_page = (text_addr % PAGE_SIZE + nblock) /  PAGE_SIZE + ((text_addr % PAGE_SIZE + nblock) % PAGE_SIZE == 0 ? 0 : 1);                                                                                      
    __android_log_print(ANDROID_LOG_INFO, "JNITag", "offset = 0x%x, e_entry = 0x%x, nblock =  0x%x, num_page = %d, base =  0x%x", ehdr32->e_shoff, ehdr32->e_entry, nblock, num_page, text_addr);
    __android_log_print(ANDROID_LOG_INFO, "JNITag", "11111");

    //修改内存的操作权限
    if(mprotect((char *) (text_addr / PAGE_SIZE * PAGE_SIZE), PAGE_SIZE * num_page, PROT_READ | PROT_EXEC | PROT_WRITE) != 0){
        __android_log_print(ANDROID_LOG_INFO, "JNITag", "mem privilege change failed");
    }
    __android_log_print(ANDROID_LOG_INFO, "JNITag", "22222");
    //解密
//    sleep(20)             ;
//    while(1){};                        

    for(i = 0; i < nblock; i++){
        __android_log_print(ANDROID_LOG_INFO, "JNITag", "i =  %d", i);
        char *addr = (char*)(text_addr + i);
        *addr = ~(*addr);
    }
    __android_log_print(ANDROID_LOG_INFO, "JNITag", "33333");

    if(mprotect((char *) (text_addr / PAGE_SIZE * PAGE_SIZE), PAGE_SIZE * num_page, PROT_READ | PROT_EXEC) != 0){
        __android_log_print(ANDROID_LOG_INFO, "JNITag", "mem privilege change failed");
    }
    __android_log_print(ANDROID_LOG_INFO, "JNITag", "Decrypt success");
}

unsigned long getLibAddr(){
    unsigned long ret = 0;
    char name[] = "libMyJni.so";
    char buf[4096], *temp;
    int pid;
    FILE *fp;
    pid = getpid();
    sprintf(buf, "/proc/%d/maps", pid);
    __android_log_print(ANDROID_LOG_INFO, "JNITag", "/proc/%d/maps", pid);
    fp = fopen(buf, "r");
    if(fp == NULL)
    {
        puts("open failed");
        goto _error;
    }
    while(fgets(buf, sizeof(buf), fp)){
        if(strstr(buf, name)){
            temp = strtok(buf, "-");
            ret = strtoul(temp, NULL, 16);
            break;
        }
    }
    _error:
    fclose(fp);
    return ret;
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       

/**
 *加密块
 *
 * */
jstring getString(JNIEnv*) __attribute__((section ("mytext")));
jstring getString(JNIEnv* env){
    return env->NewStringUTF("Native method return!");
};