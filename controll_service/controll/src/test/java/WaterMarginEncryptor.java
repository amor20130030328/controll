import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 水浒传108将Java代码加密解密工具
 * 核心规则：108将排名编码(0-107) + 天罡地煞位置置换 + Java语法专属标识
 */
public class WaterMarginEncryptor {
    // 分隔符：字符编码边界
    private static final String SEPARATOR = "★";
    // 换行标识：替代Java代码中的\n/\r\n
    private static final String NEW_LINE = "●";
    // Java关键字标识：替天行道
    private static final String KEY_WORD_FLAG = "■";
    // 单行注释标识：智多星
    private static final String SINGLE_COMMENT_FLAG = "♠";
    // 语法符号包裹符：梁山
    private static final String SYNTAX_WRAP = "▲";
    // 天罡标识：ASCII 108-127
    private static final String TIAN_GANG = "T";
    // 地煞标识：非ASCII字符（中文/全角等）
    private static final String DI_SHA = "D";
    // Java核心关键字（覆盖常规开发，可按需扩展）
    private static final String[] JAVA_KEYWORDS = {
            "public", "private", "protected", "class", "interface", "enum",
            "static", "final", "void", "int", "String", "boolean", "char",
            "double", "float", "long", "if", "else", "for", "while", "main",
            "return", "new", "this", "super", "try", "catch", "throws"
    };
    // Java核心语法符号（覆盖常规开发，可按需扩展）
    private static final char[] JAVA_SYNTAX_CHARS = {
            '{', '}', '(', ')', ';', '=', '+', '-', '*', '/', '%', '.', ',',
            '[', ']', '&', '|', '!', '>', '<', '?', ':', '"', '\'', '#'
    };

    // 正则：匹配单行注释//
    private static final Pattern SINGLE_COMMENT_PATTERN = Pattern.compile("//.*");
    // 正则：匹配语法符号包裹的编码 ▲xxx▲
    private static final Pattern SYNTAX_PATTERN = Pattern.compile("▲(.*?)▲");
    // 正则：匹配天罡编码 T+数字
    private static final Pattern TG_PATTERN = Pattern.compile(TIAN_GANG + "\\d+");
    // 正则：匹配地煞编码 D+数字(Unicode)
    private static final Pattern DS_PATTERN = Pattern.compile(DI_SHA + "\\d+");

    /**
     * 加密方法：Java代码 → 108将加密串
     * @param javaCode 原始Java代码
     * @return 加密后的字符串
     */
    public String encrypt(String javaCode) {
        if (javaCode == null || javaCode.isEmpty()) {
            return "";
        }
        // 步骤1：预处理（替换换行、标记单行注释、标记关键字、标记语法符号）
        String preCode = preProcessEncrypt(javaCode);
        // 步骤2：字符基础转码（字符→108将编码/T/D标识）
        List<String> codeList = char2Code(preCode);
        // 步骤3：天罡地煞位置置换（36倍数位交换、72倍数位+108）
        List<String> replaceList = tgDsReplace(codeList);
        // 步骤4：拼接分隔符，生成最终加密串
        return String.join(SEPARATOR, replaceList);
    }

    /**
     * 解密方法：108将加密串 → 原始Java代码
     * @param encryptStr 加密后的字符串
     * @return 解密后的原始Java代码
     */
    public String decrypt(String encryptStr) {
        if (encryptStr == null || encryptStr.isEmpty()) {
            return "";
        }
        // 步骤1：拆分编码，去除分隔符
        List<String> codeList = splitEncryptStr(encryptStr);
        // 步骤2：天罡地煞位置还原（72倍数位-108、36倍数位交换回原位置）
        List<String> restoreList = tgDsRestore(codeList);
        // 步骤3：编码转字符（108将编码/T/D→原始字符）
        String rawCode = code2Char(restoreList);
        // 步骤4：后处理（还原换行、去除标识、恢复语法符号）
        return postProcessDecrypt(rawCode);
    }

    // ---------------------- 加密前置处理 ----------------------
    /**
     * 加密预处理：标记注释/关键字/语法符号，替换换行
     */
    private String preProcessEncrypt(String javaCode) {
        StringBuilder sb = new StringBuilder();
        // 1. 替换换行符为NEW_LINE标识
        String code = javaCode.replaceAll("\\r\\n|\\r|\\n", NEW_LINE);
        char[] chars = code.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            // 2. 标记单行注释//，添加SINGLE_COMMENT_FLAG
            if (c == '/' && i + 1 < chars.length && chars[i + 1] == '/') {
                sb.append(SINGLE_COMMENT_FLAG);
                sb.append(c);
                sb.append(chars[i + 1]);
                i++; // 跳过下一个/
            }
            // 3. 标记语法符号，用SYNTAX_WRAP包裹
            else if (isSyntaxChar(c)) {
                sb.append(SYNTAX_WRAP).append(c).append(SYNTAX_WRAP);
            }
            // 4. 标记Java关键字，添加KEY_WORD_FLAG（匹配完整关键字）
            else if (Character.isLetter(c)) {
                int end = i;
                while (end < chars.length && (Character.isLetterOrDigit(chars[end]) || chars[end] == '_')) {
                    end++;
                }
                String word = code.substring(i, end);
                if (isJavaKeyword(word)) {
                    sb.append(KEY_WORD_FLAG);
                }
                sb.append(word);
                i = end - 1; // 跳过分词后的位置
            }
            // 普通字符直接追加
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // ---------------------- 字符转108将编码 ----------------------
    /**
     * 字符转编码：核心转码规则，0-107直接转，108-127转T+偏移，非ASCII转D+Unicode
     */
    private List<String> char2Code(String preCode) {
        List<String> codeList = new ArrayList<>();
        char[] chars = preCode.toCharArray();
        for (char c : chars) {
            int ascii = (int) c;
            if (ascii >= 0 && ascii <= 107) {
                // 0-107：直接映射108将编码（排名=ascii+1）
                codeList.add(String.valueOf(ascii));
            } else if (ascii >= 108 && ascii <= 127) {
                // 108-127：天罡T + 偏移量（ascii-107）
                int offset = ascii - 107;
                codeList.add(TIAN_GANG + offset);
            } else {
                // 非ASCII（中文/全角）：地煞D + 十进制Unicode
                codeList.add(DI_SHA + (int) c);
            }
        }
        return codeList;
    }

    // ---------------------- 天罡地煞位置置换 ----------------------
    /**
     * 加密置换：36倍数位交换后一位，72倍数位+108
     */
    private List<String> tgDsReplace(List<String> codeList) {
        List<String> newList = new ArrayList<>(codeList);
        int size = newList.size();
        for (int i = 0; i < size; i++) {
            String code = newList.get(i);
            // 72倍数位：编码+108（先处理，避免交换后影响）
            if (i % 72 == 0) {
                newList.set(i, handleAdd108(code));
            }
            // 36倍数位：与后一位交换（最后一位不交换）
            if (i % 36 == 0 && i + 1 < size) {
                String nextCode = newList.get(i + 1);
                newList.set(i + 1, newList.get(i));
                newList.set(i, nextCode);
            }
        }
        return newList;
    }

    // ---------------------- 解密拆分加密串 ----------------------
    /**
     * 拆分加密串：按分隔符拆分，过滤空值
     */
    private List<String> splitEncryptStr(String encryptStr) {
        List<String> codeList = new ArrayList<>();
        String[] arr = encryptStr.split(SEPARATOR);
        for (String s : arr) {
            if (!s.isEmpty()) {
                codeList.add(s);
            }
        }
        return codeList;
    }

    // ---------------------- 天罡地煞位置还原 ----------------------
    /**
     * 解密还原：36倍数位交换回原位置，72倍数位-108
     */
    private List<String> tgDsRestore(List<String> codeList) {
        List<String> newList = new ArrayList<>(codeList);
        int size = newList.size();
        for (int i = 0; i < size; i++) {
            // 36倍数位：先交换回原位置（最后一位不交换）
            if (i % 36 == 0 && i + 1 < size) {
                String nextCode = newList.get(i + 1);
                newList.set(i + 1, newList.get(i));
                newList.set(i, nextCode);
            }
            // 72倍数位：编码-108还原
            if (i % 72 == 0) {
                newList.set(i, handleMinus108(newList.get(i)));
            }
        }
        return newList;
    }

    // ---------------------- 编码转字符 ----------------------
    /**
     * 编码转字符：核心还原规则，反向解析108将编码/T/D标识
     */
    private String code2Char(List<String> restoreList) {
        StringBuilder sb = new StringBuilder();
        for (String code : restoreList) {
            if (TG_PATTERN.matcher(code).matches()) {
                // 天罡T：解析偏移量，还原ASCII（107+偏移）
                int offset = Integer.parseInt(code.substring(1));
                sb.append((char) (107 + offset));
            } else if (DS_PATTERN.matcher(code).matches()) {
                // 地煞D：解析Unicode，还原字符
                int unicode = Integer.parseInt(code.substring(1));
                sb.append((char) unicode);
            } else {
                // 108将编码：直接还原ASCII
                try {
                    int ascii = Integer.parseInt(code);
                    sb.append((char) ascii);
                } catch (NumberFormatException e) {
                    // 异常编码直接保留（防止加密串被篡改）
                    sb.append(code);
                }
            }
        }
        return sb.toString();
    }

    // ---------------------- 解密后置处理 ----------------------
    /**
     * 解密后处理：还原换行、去除各类标识、恢复语法符号
     */
    private String postProcessDecrypt(String rawCode) {
        StringBuilder sb = new StringBuilder(rawCode);
        // 1. 还原换行：NEW_LINE标识→\n
        String temp = sb.toString().replace(NEW_LINE, "\n");
        // 2. 去除关键字标识
        temp = temp.replace(KEY_WORD_FLAG, "");
        // 3. 去除单行注释标识
        temp = temp.replace(SINGLE_COMMENT_FLAG, "");
        // 4. 去除语法符号包裹符
        temp = temp.replace(SYNTAX_WRAP, "");
        return temp;
    }

    // ---------------------- 工具方法 ----------------------
    /**
     * 判断是否为Java关键字
     */
    private boolean isJavaKeyword(String word) {
        for (String kw : JAVA_KEYWORDS) {
            if (kw.equals(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为Java语法符号
     */
    private boolean isSyntaxChar(char c) {
        for (char sc : JAVA_SYNTAX_CHARS) {
            if (sc == c) {
                return sc == c;
            }
        }
        return false;
    }

    /**
     * 72倍数位处理：编码+108（兼容T/D标识）
     */
    private String handleAdd108(String code) {
        if (code.startsWith(TIAN_GANG)) {
            int num = Integer.parseInt(code.substring(1));
            return TIAN_GANG + (num + 108);
        } else if (code.startsWith(DI_SHA)) {
            long num = Long.parseLong(code.substring(1));
            return DI_SHA + (num + 108);
        } else {
            int num = Integer.parseInt(code);
            return String.valueOf(num + 108);
        }
    }

    /**
     * 72倍数位还原：编码-108（兼容T/D标识）
     */
    private String handleMinus108(String code) {
        if (code.startsWith(TIAN_GANG)) {
            int num = Integer.parseInt(code.substring(1));
            return TIAN_GANG + (num - 108);
        } else if (code.startsWith(DI_SHA)) {
            long num = Long.parseLong(code.substring(1));
            return DI_SHA + (num - 108);
        } else {
            int num = Integer.parseInt(code);
            return String.valueOf(num - 108);
        }
    }

    // ---------------------- 测试主方法 ----------------------
    public static void main(String[] args) throws IOException {
        // 初始化加密工具
        WaterMarginEncryptor encryptor = new WaterMarginEncryptor();

        String javaCode = "";
        BufferedReader bf = null;
        try {
            String path = "D:\\code\\controll_service\\controll\\src\\main\\resources\\input";
            bf = new BufferedReader(new FileReader(path));
            List<String> list = bf.lines().collect(Collectors.toList());
            javaCode = String.join("", list);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }finally {

        }
        // 测试用的Java代码（含注释、关键字、语法符号、中文）


        // 加密
        String encryptStr = encryptor.encrypt(javaCode);
        System.out.println("===== 108将加密串 =====");
        System.out.println(encryptStr);
        System.out.println("\\n===== 解密还原的Java代码 =====");


        // 解密
        String decryptCode = encryptor.decrypt(encryptStr);
        System.out.println(decryptCode);

    }
}