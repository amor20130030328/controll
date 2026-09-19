import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Java代码加密工具：
 * 1. Java关键字/符号/JDK内置类/方法 → 易理解中文
 * 2. 自定义变量/类名（标识符）→ var1、var2、var3...（有序通用命名）
 * 3. 解密可完整还原原始代码
 */
public class JavaCodeEncryptor {
    // 1. 核心映射字典：Java关键字/符号/JDK内置 → 易理解中文
    private static final Map<String, String> ENCRYPT_DICT = new HashMap<>();
    private static final Map<String, String> DECRYPT_DICT = new HashMap<>();

    // 2. 排除列表：不需要替换为var的内容（Java关键字+JDK内置名）
    private static final Set<String> EXCLUDE_SET = new HashSet<>();

    // 初始化字典和排除列表
    static {
        // ===== 1. 填充映射字典 =====
        // Java关键字
        ENCRYPT_DICT.put("public", "公开");
        ENCRYPT_DICT.put("private", "私有");
        ENCRYPT_DICT.put("static", "静态");
        ENCRYPT_DICT.put("void", "无返回");
        ENCRYPT_DICT.put("int", "整数");
        ENCRYPT_DICT.put("String", "字符串");
        ENCRYPT_DICT.put("List", "列表");
        ENCRYPT_DICT.put("Map", "映射表");
        ENCRYPT_DICT.put("for", "循环");
        ENCRYPT_DICT.put("if", "判断");
        ENCRYPT_DICT.put("return", "返回");
        ENCRYPT_DICT.put("new", "新建");
        ENCRYPT_DICT.put("class", "类");

        // Java符号
        ENCRYPT_DICT.put("=", "等于");
        ENCRYPT_DICT.put("+", "加");
        ENCRYPT_DICT.put("-", "减");
        ENCRYPT_DICT.put("*", "乘");
        ENCRYPT_DICT.put("/", "除");
        ENCRYPT_DICT.put(";", "结束符");
        ENCRYPT_DICT.put("{", "开始块");
        ENCRYPT_DICT.put("}", "结束块");
        ENCRYPT_DICT.put("(", "左括号");
        ENCRYPT_DICT.put(")", "右括号");
        ENCRYPT_DICT.put("[", "左方括号");
        ENCRYPT_DICT.put("]", "右方括号");
        ENCRYPT_DICT.put(".", "点");
        ENCRYPT_DICT.put(",", "分隔符");

        // JDK内置类/方法
        ENCRYPT_DICT.put("System", "系统");
        ENCRYPT_DICT.put("out", "输出");
        ENCRYPT_DICT.put("println", "打印换行");
        ENCRYPT_DICT.put("main", "主方法");
        ENCRYPT_DICT.put("args", "参数数组");

        // ===== 2. 构建解密字典 =====
        for (Map.Entry<String, String> entry : ENCRYPT_DICT.entrySet()) {
            DECRYPT_DICT.put(entry.getValue(), entry.getKey());
        }

        // ===== 3. 填充排除列表（不替换为var的内容）=====
        EXCLUDE_SET.addAll(ENCRYPT_DICT.keySet());
        EXCLUDE_SET.add("println(");
        EXCLUDE_SET.add("size()");
        EXCLUDE_SET.add("");
        // 补充数字、中文等非标识符内容
        EXCLUDE_SET.add("108");
        EXCLUDE_SET.add("水浒传");
    }

    /**
     * 提取代码中的所有自定义标识符，并分配var编号（var1、var2...）
     * @param code 原始Java代码
     * @return 原始标识符 → var编号的映射表
     */
    private static Map<String, String> extractIdentifiers(String code) {
        Map<String, String> identifierVarMap = new LinkedHashMap<>(); // 保持插入顺序
        Pattern identifierPattern = Pattern.compile("\\b[a-zA-Z_][a-zA-Z0-9_]*\\b");
        Matcher matcher = identifierPattern.matcher(code);
        int varCount = 1;

        while (matcher.find()) {
            String identifier = matcher.group();
            // 排除关键字、JDK内置名、数字等，仅处理自定义标识符
            if (!EXCLUDE_SET.contains(identifier) && !identifier.matches("\\d+") && !identifier.matches("[\\u4e00-\\u9fa5]+")) {
                if (!identifierVarMap.containsKey(identifier)) {
                    identifierVarMap.put(identifier, "var" + varCount);
                    varCount++;
                }
            }
        }
        return identifierVarMap;
    }

    /**
     * 加密Java代码
     * 规则：关键字/符号/JDK内置→中文；自定义标识符→var1、var2...
     */
    public static String encrypt(String originalCode) {
        if (originalCode == null || originalCode.isEmpty()) {
            return "";
        }
        String encryptedCode = originalCode;

        // ===== 步骤1：提取自定义标识符并分配var编号 =====
        Map<String, String> identifierVarMap = extractIdentifiers(originalCode);

        // ===== 步骤2：替换自定义标识符为var编号 =====
        for (Map.Entry<String, String> entry : identifierVarMap.entrySet()) {
            String originalId = entry.getKey();
            String varId = entry.getValue();
            // 正则匹配完整标识符，避免部分匹配
            String regex = "\\b" + Pattern.quote(originalId) + "\\b";
            encryptedCode = encryptedCode.replaceAll(regex, varId);
        }

        // ===== 步骤3：替换Java关键字/符号/JDK内置为中文（长内容优先）=====
        List<String> sortedKeys = new ArrayList<>(ENCRYPT_DICT.keySet());
        sortedKeys.sort((a, b) -> b.length() - a.length());
        for (String key : sortedKeys) {
            String replacement = ENCRYPT_DICT.get(key);
            String regex = "\\b" + Pattern.quote(key) + "\\b";
            // 对带括号的方法名（如println(）去掉单词边界
            if (key.contains("(")) {
                regex = Pattern.quote(key);
            }
            encryptedCode = encryptedCode.replaceAll(regex, replacement);
        }

        return encryptedCode;
    }

    /**
     * 解密密文：先还原var编号为原始标识符，再还原中文为原始Java内容
     */
    public static String decrypt(String encryptedCode, Map<String, String> identifierVarMap) {
        if (encryptedCode == null || encryptedCode.isEmpty() || identifierVarMap == null) {
            return "";
        }
        String decryptedCode = encryptedCode;

        // ===== 步骤1：还原var编号为原始标识符（反向映射）=====
        // 构建var编号→原始标识符的映射
        Map<String, String> varIdentifierMap = new HashMap<>();
        for (Map.Entry<String, String> entry : identifierVarMap.entrySet()) {
            varIdentifierMap.put(entry.getValue(), entry.getKey());
        }
        // 按var编号长度倒序，避免var10覆盖var1
        List<String> sortedVarIds = new ArrayList<>(varIdentifierMap.keySet());
        sortedVarIds.sort((a, b) -> b.length() - a.length());
        for (String varId : sortedVarIds) {
            String originalId = varIdentifierMap.get(varId);
            String regex = "\\b" + Pattern.quote(varId) + "\\b";
            decryptedCode = decryptedCode.replaceAll(regex, originalId);
        }

        // ===== 步骤2：还原中文为原始Java关键字/符号/JDK内置 =====
        List<String> sortedValues = new ArrayList<>(DECRYPT_DICT.keySet());
        sortedValues.sort((a, b) -> b.length() - a.length());
        for (String value : sortedValues) {
            String replacement = DECRYPT_DICT.get(value);
            String regex = Pattern.quote(value);
            decryptedCode = decryptedCode.replaceAll(regex, replacement);
        }

        return decryptedCode;
    }

    // 测试示例
    public static void main(String[] args) {
        // 原始代码（包含多个自定义变量/类名）
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

        // ===== 加密 =====
        // 先提取标识符映射（用于解密）
        Map<String, String> identifierVarMap = extractIdentifiers(javaCode);
        String encryptedCode = encrypt(javaCode);
        System.out.println("===== 加密后的密文（自定义变量为var1/var2...） =====");
        System.out.println(encryptedCode);

        // ===== 解密 =====
        String decryptedCode = decrypt(encryptedCode, identifierVarMap);
        System.out.println("===== 解密后的原始代码 =====");
        System.out.println(decryptedCode);

        // 打印标识符映射表（验证var分配规则）
        System.out.println("===== 标识符-var映射表 =====");
        identifierVarMap.forEach((k, v) -> System.out.println(k + " → " + v));
    }
}