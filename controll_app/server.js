const express = require('express');
const cors = require('cors');
const { exec } = require('child_process');
const path = require('path');

const app = express();
const PORT = 3001;

app.use(cors());
app.use(express.json());

// MAC OUI 厂商前缀数据库（常见手机厂商）
const OUI_DB = {
    // Apple
    'ac:bc:32': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'f4:f1:5a': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'a8:5c:2c': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '00:cd:fe': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '04:26:65': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '04:4b:ed': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '04:52:f3': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '04:d3:cf': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '08:6d:41': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '0c:74:c2': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '10:41:7f': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '14:99:e2': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '18:af:61': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '1c:91:48': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '20:76:8f': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '24:a0:74': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '28:cf:e9': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '2c:1f:23': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '34:08:bc': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '38:c9:86': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '3c:15:c2': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '40:4d:7f': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '44:00:10': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '48:60:bc': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '4c:57:ca': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '50:ea:d6': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '54:72:4f': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '58:b0:35': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '5c:97:f3': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '60:f4:45': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '64:9a:be': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '68:96:7b': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '6c:40:08': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '70:56:81': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '74:e1:b6': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '78:7b:8a': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '7c:04:d0': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '80:be:05': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '84:38:35': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '88:1f:a1': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '8c:00:6d': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '90:72:40': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '94:bf:2d': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '98:01:a7': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    '9c:f3:87': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'a0:99:9b': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'a4:5e:60': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'a8:86:dd': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'ac:de:48': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'b0:65:bd': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'b4:8b:19': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'b8:c7:5d': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'bc:92:6b': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'c0:63:94': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'c4:2c:03': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'c8:e0:eb': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'cc:44:63': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'd0:03:4b': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'd4:61:9d': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'd8:1d:72': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'dc:2b:2a': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'e0:b9:ba': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'e4:25:e7': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'e8:06:88': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'ec:35:86': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'f0:18:98': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'f4:37:b7': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'f8:27:93': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    'fc:fc:48': { vendor: 'Apple', os: 'iOS', deviceType: '手机/平板' },
    // 小米
    '00:9e:c8': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '08:21:ef': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '10:2a:b3': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '28:6c:07': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '34:80:b3': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '38:a4:ed': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '50:64:2b': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '58:44:98': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '64:09:80': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '68:df:dd': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '74:51:ba': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '78:02:f8': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '8c:be:be': { vendor: '小米', os: 'Android', deviceType: '手机' },
    '98:fa:e3': { vendor: '小米', os: 'Android', deviceType: '手机' },
    'ac:f7:f3': { vendor: '小米', os: 'Android', deviceType: '手机' },
    'b0:e2:35': { vendor: '小米', os: 'Android', deviceType: '手机' },
    'c4:0b:cb': { vendor: '小米', os: 'Android', deviceType: '手机' },
    'f0:b4:29': { vendor: '小米', os: 'Android', deviceType: '手机' },
    'f4:8e:38': { vendor: '小米', os: 'Android', deviceType: '手机' },
    'fc:64:ba': { vendor: '小米', os: 'Android', deviceType: '手机' },
    // 华为
    '00:46:4b': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '04:02:1f': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '04:f9:38': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '08:19:a6': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '0c:37:dc': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '14:b9:68': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '1c:1d:67': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '20:a6:80': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '28:31:52': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '2c:ab:00': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '34:29:12': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '38:37:8b': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '3c:df:a9': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '40:4e:36': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '48:db:50': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '4c:1b:86': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '50:a7:2b': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '54:89:98': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '5c:c3:07': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '60:83:34': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '68:3e:34': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '6c:8d:c1': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '70:72:3c': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '74:a5:28': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '78:1d:ba': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '80:38:bc': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '84:db:ac': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '88:e3:ab': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '90:67:1c': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '94:77:2b': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '98:e7:f4': { vendor: '华为', os: 'Android', deviceType: '手机' },
    '9c:28:ef': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'a0:08:6f': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'ac:e8:7b': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'b4:86:55': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'bc:25:e0': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'c4:86:e9': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'c8:14:79': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'cc:a2:23': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'd0:7a:b5': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'd4:6e:5c': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'd8:c7:71': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'dc:d2:fc': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'e0:19:54': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'e4:a4:71': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'e8:cd:2d': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'ec:23:3d': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'f4:43:5a': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'f8:3d:ff': { vendor: '华为', os: 'Android', deviceType: '手机' },
    'fc:87:43': { vendor: '华为', os: 'Android', deviceType: '手机' },
    // OPPO/一加
    '00:1a:ef': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '00:e0:d0': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '04:d6:aa': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '14:7d:c5': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '1c:77:f6': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '20:47:da': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '2c:5b:b8': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '34:d0:b8': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '3c:28:6d': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '40:cb:c0': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '48:a4:72': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '4c:03:4f': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '50:fc:9f': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '54:f2:01': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '60:9a:c1': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '68:eb:c5': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '6c:5a:b0': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '8c:eb:c6': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    '90:03:b7': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    'a4:f3:c1': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    'bc:f1:71': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    'c0:ee:fb': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    'c4:57:6e': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    'dc:05:ed': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    'e4:60:58': { vendor: 'OPPO', os: 'Android', deviceType: '手机' },
    // vivo
    '00:16:e3': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '08:d4:6a': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '14:3f:a6': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '1c:16:2d': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '28:ba:b5': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '2c:f0:a2': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '38:68:a4': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '40:31:3c': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '48:57:02': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '50:2a:2b': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '58:a2:b5': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '60:f1:89': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '6c:3d:24': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '70:8b:cd': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '74:2a:68': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '78:da:07': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '80:89:17': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '84:db:2f': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '8c:eb:c6': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    '90:3c:92': { vendor: 'vivo', os: 'Android', deviceType: '手机' },
    // 三星
    '00:00:f0': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '00:02:78': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '00:12:47': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '00:15:b9': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '00:17:d5': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '00:1a:8a': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '00:1d:25': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '00:21:d2': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '00:23:d6': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '00:24:54': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '00:26:37': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '10:d5:42': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '18:22:7e': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '20:13:e0': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '28:98:7b': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '30:19:66': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '38:16:d1': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '40:0e:85': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '50:01:bb': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '5c:3c:27': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '68:27:37': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '78:40:e4': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '84:25:db': { vendor: '三星', os: 'Android', deviceType: '手机' },
    '90:18:7c': { vendor: '三星', os: 'Android', deviceType: '手机' },
    'a0:0b:ba': { vendor: '三星', os: 'Android', deviceType: '手机' },
    'b4:3a:28': { vendor: '三星', os: 'Android', deviceType: '手机' },
    'c0:bd:d1': { vendor: '三星', os: 'Android', deviceType: '手机' },
    'd0:22:be': { vendor: '三星', os: 'Android', deviceType: '手机' },
    'e8:50:8b': { vendor: '三星', os: 'Android', deviceType: '手机' },
    'f8:db:88': { vendor: '三星', os: 'Android', deviceType: '手机' },
    // 路由器常见前缀
    'e4:66:ab': { vendor: '路由器', os: '未知', deviceType: '路由器/网关' },
    '00:50:ba': { vendor: '路由器', os: '未知', deviceType: '路由器/网关' },
};

function getDeviceInfo(mac) {
    if (!mac) return { vendor: '未知', os: '未知', deviceType: '未知' };
    const normalized = mac.toLowerCase().replace(/-/g, ':');
    const oui = normalized.substring(0, 8);
    if (OUI_DB[oui]) return OUI_DB[oui];
    return { vendor: '未知', os: '未知', deviceType: '未知' };
}

// 检查nginx状态
app.get('/api/nginx/status', (req, res) => {
    const nginxPath = path.join(__dirname, 'nginx-1.16.1', 'nginx.exe');

    exec('tasklist /FI "IMAGENAME eq nginx.exe"', (error, stdout, stderr) => {
        if (error) {
            return res.json({ running: false, message: '检查失败' });
        }

        const isRunning = stdout.includes('nginx.exe');
        res.json({
            running: isRunning,
            message: isRunning ? 'Nginx正在运行' : 'Nginx未运行'
        });
    });
});

// 启动nginx
app.post('/api/nginx/start', (req, res) => {
    const nginxPath = path.join(__dirname, 'nginx-1.16.1');
    const nginxExe = path.join(nginxPath, 'nginx.exe');

    exec('tasklist /FI "IMAGENAME eq nginx.exe"', (error, stdout, stderr) => {
        if (stdout.includes('nginx.exe')) {
            return res.json({ success: false, message: 'Nginx已经在运行中' });
        }

        const { spawn } = require('child_process');
        const child = spawn('cmd.exe', ['/c', 'start', '/B', 'nginx.exe'], {
            cwd: nginxPath,
            detached: true,
            stdio: 'ignore'
        });

        child.unref();

        setTimeout(() => {
            exec('tasklist /FI "IMAGENAME eq nginx.exe"', (err, out) => {
                const started = out.includes('nginx.exe');
                if (!started) {
                    console.log('Nginx可能需要更多时间启动');
                }
            });
        }, 500);

        res.json({ success: true, message: 'Nginx启动命令已执行' });
    });
});

// 停止nginx
app.post('/api/nginx/stop', (req, res) => {
    const nginxPath = path.join(__dirname, 'nginx-1.16.1');
    const nginxExe = path.join(nginxPath, 'nginx.exe');

    const { spawn } = require('child_process');
    const stopProcess = spawn(nginxExe, ['-s', 'stop'], {
        cwd: nginxPath,
        windowsHide: true
    });

    let errorOutput = '';

    stopProcess.stderr.on('data', (data) => {
        errorOutput += data.toString();
    });

    stopProcess.on('close', (code) => {
        setTimeout(() => {
            exec('tasklist /FI "IMAGENAME eq nginx.exe"', (err, out) => {
                const stopped = !out.includes('nginx.exe');
                if (stopped) {
                    res.json({ success: true, message: 'Nginx已停止' });
                } else {
                    res.json({ success: false, message: `停止失败: ${errorOutput || '未知错误'}` });
                }
            });
        }, 500);
    });
});

// 删除占用8080端口的进程
app.post('/api/port/kill', (req, res) => {
    const port = req.body.port || 8080;
    exec(`netstat -ano | findstr :${port}`, (error, stdout, stderr) => {
        if (error || !stdout.trim()) {
            return res.json({ success: false, message: `未找到占用端口 ${port} 的进程` });
        }

        const lines = stdout.trim().split('\n');
        const pids = new Set();
        lines.forEach(line => {
            const parts = line.trim().split(/\s+/);
            const pid = parts[parts.length - 1];
            if (pid && /^\d+$/.test(pid) && pid !== '0') {
                pids.add(pid);
            }
        });

        if (pids.size === 0) {
            return res.json({ success: false, message: `未找到占用端口 ${port} 的进程` });
        }

        let killed = [];
        let failed = [];

        const pidList = [...pids];
        const os = require('os');
        const fs = require('fs');
        const resultFile = path.join(os.tmpdir(), `kill_result_${Date.now()}.txt`);
        const scriptFile = path.join(os.tmpdir(), `kill_script_${Date.now()}.ps1`);

        const scriptContent = pidList.map(pid =>
            `try { Stop-Process -Id ${pid} -Force -ErrorAction Stop; Add-Content '${resultFile.replace(/\\/g, '\\\\')}' 'killed:${pid}' } catch { taskkill /F /PID ${pid} 2>$null; if ($LASTEXITCODE -eq 0) { Add-Content '${resultFile.replace(/\\/g, '\\\\')}' 'killed:${pid}' } else { Add-Content '${resultFile.replace(/\\/g, '\\\\')}' 'failed:${pid}' } }`
        ).join("\n");

        fs.writeFileSync(scriptFile, scriptContent, 'utf8');

        exec(
            `powershell -NonInteractive -Command "Start-Process powershell -ArgumentList '-NonInteractive -ExecutionPolicy Bypass -File \\"${scriptFile}\\"' -Verb RunAs -Wait -WindowStyle Hidden"`,
            { timeout: 15000 },
            (err1) => {
                setTimeout(() => {
                    let resultContent = '';
                    try { resultContent = fs.readFileSync(resultFile, 'utf8'); } catch(e) {}
                    try { fs.unlinkSync(scriptFile); } catch(e) {}
                    try { fs.unlinkSync(resultFile); } catch(e) {}

                    if (resultContent) {
                        resultContent.trim().split('\n').forEach(line => {
                            const [status, pid] = line.trim().split(':');
                            if (status === 'killed') killed.push(pid);
                            else if (status === 'failed') failed.push(pid);
                        });
                    } else {
                        pidList.forEach(pid => failed.push(pid));
                    }
                    finalize();
                }, 1000);
            }
        );

        function finalize() {
            if (killed.length > 0) {
                res.json({ success: true, message: `已终止进程 PID: ${killed.join(', ')}`, killed, failed });
            } else {
                res.json({ success: false, message: `终止进程失败，请以管理员身份运行服务器后重试。PID: ${failed.join(', ')}`, killed, failed });
            }
        }
    });
});

// 查询指定端口占用情况
app.get('/api/port/status', (req, res) => {
    const port = req.query.port || 8080;
    exec(`netstat -ano | findstr :${port}`, (error, stdout, stderr) => {
        if (error || !stdout.trim()) {
            return res.json({ occupied: false, message: `端口 ${port} 未被占用`, processes: [] });
        }

        const lines = stdout.trim().split('\n');
        const pids = new Set();
        lines.forEach(line => {
            const parts = line.trim().split(/\s+/);
            const pid = parts[parts.length - 1];
            if (pid && /^\d+$/.test(pid) && pid !== '0') {
                pids.add(pid);
            }
        });

        res.json({ occupied: pids.size > 0, message: `端口 ${port} 被以下PID占用`, processes: [...pids] });
    });
});

// 扫描局域网IP（含设备类型识别）
app.get('/api/network/scan', (req, res) => {
    const os = require('os');

    const interfaces = os.networkInterfaces();
    let localIPs = [];
    let subnet = null;

    for (const name of Object.keys(interfaces)) {
        for (const iface of interfaces[name]) {
            if (iface.family === 'IPv4' && !iface.internal) {
                localIPs.push(iface.address);
                if (!subnet) {
                    const parts = iface.address.split('.');
                    subnet = `${parts[0]}.${parts[1]}.${parts[2]}`;
                }
            }
        }
    }

    if (!subnet) {
        return res.json({ success: false, message: '无法获取本机IP', hosts: [] });
    }

    exec(`ping -n 1 -w 100 ${subnet}.255 >nul 2>&1 & arp -a`, { timeout: 15000 }, (error, stdout, stderr) => {
        const hosts = [];
        const seen = new Set();

        const lines = stdout.split('\n');
        lines.forEach(line => {
            const match = line.match(/(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})/);
            if (match) {
                const ip = match[1];
                if (ip.startsWith(subnet) && !seen.has(ip) && !ip.endsWith('.255') && !ip.endsWith('.0')) {
                    const macMatch = line.match(/([0-9a-fA-F]{2}[:-]){5}[0-9a-fA-F]{2}/);
                    const mac = macMatch ? macMatch[0] : '';
                    const typeMatch = line.match(/(dynamic|static|动态|静态)/i);
                    const type = typeMatch ? typeMatch[0] : '';
                    const isLocal = localIPs.includes(ip);
                    const deviceInfo = getDeviceInfo(mac);
                    seen.add(ip);
                    hosts.push({ ip, mac, type, isLocal, ...deviceInfo });
                }
            }
        });

        localIPs.forEach(ip => {
            if (!seen.has(ip)) {
                hosts.push({ ip, mac: '', type: 'local', isLocal: true, vendor: '本机', os: 'Windows', deviceType: '电脑' });
            }
        });

        hosts.sort((a, b) => {
            const aLast = parseInt(a.ip.split('.').pop());
            const bLast = parseInt(b.ip.split('.').pop());
            return aLast - bLast;
        });

        res.json({ success: true, subnet, localIPs, hosts });
    });
});

// ADB 连接设备
app.post('/api/adb/connect', (req, res) => {
    const { ip, port } = req.body;
    const adbPort = port || 5555;
    const adbPath = path.join(__dirname, '..', 'tools', 'adb', 'adb.exe');

    exec(`"${adbPath}" connect ${ip}:${adbPort}`, { timeout: 10000 }, (error, stdout, stderr) => {
        const output = (stdout + stderr).trim();
        const success = output.includes('connected') && !output.includes('failed') && !output.includes('unable');
        res.json({ success, message: output || (error ? error.message : '连接失败') });
    });
});

// ADB 获取已连接设备列表
app.get('/api/adb/devices', (req, res) => {
    const adbPath = path.join(__dirname, '..', 'tools', 'adb', 'adb.exe');

    exec(`"${adbPath}" devices`, { timeout: 10000 }, (error, stdout, stderr) => {
        if (error) {
            return res.json({ success: false, devices: [], message: error.message });
        }
        const lines = stdout.trim().split('\n').slice(1);
        const devices = lines
            .filter(line => line.trim() && !line.startsWith('*'))
            .map(line => {
                const parts = line.trim().split(/\s+/);
                return { id: parts[0], status: parts[1] || 'device' };
            });
        res.json({ success: true, devices });
    });
});

// ADB 获取设备上已安装的可分屏app列表
app.get('/api/adb/apps/:deviceId', (req, res) => {
    const { deviceId } = req.params;
    const adbPath = path.join(__dirname, '..', 'tools', 'adb', 'adb.exe');

    // 获取所有第三方app包名
    exec(`"${adbPath}" -s ${deviceId} shell pm list packages -3`, { timeout: 15000 }, (error, stdout, stderr) => {
        if (error) {
            return res.json({ success: false, apps: [], message: error.message });
        }
        const apps = stdout.trim().split('\n')
            .map(line => line.replace('package:', '').trim())
            .filter(pkg => pkg.length > 0);
        res.json({ success: true, apps });
    });
});

// ADB 分屏：同时显示两个app
// Android 7+ 支持通过 am stack 命令或 MultiWindow 方式实现分屏
app.post('/api/adb/splitscreen', (req, res) => {
    const { deviceId, app1, app2 } = req.body;
    if (!deviceId || !app1 || !app2) {
        return res.json({ success: false, message: '参数缺失: deviceId, app1, app2 必填' });
    }

    const adbPath = path.join(__dirname, '..', 'tools', 'adb', 'adb.exe');
    const adb = `"${adbPath}" -s ${deviceId}`;

    // Step 1: 启动第一个app（主窗口）
    const launchApp1 = `${adb} shell monkey -p ${app1} -c android.intent.category.LAUNCHER 1`;

    exec(launchApp1, { timeout: 10000 }, (err1) => {
        if (err1) {
            return res.json({ success: false, message: `启动 ${app1} 失败: ${err1.message}` });
        }

        // Step 2: 等待app1启动后，进入分屏模式（长按recent键或am命令）
        // 使用 adb shell am start --activity-launch-when-task-is-empty 方式
        // Android 9+ 推荐使用 MultiWindowMode
        setTimeout(() => {
            // 尝试通过 am 命令进入分屏（Android 7-11兼容写法）
            const enterSplitScreen = `${adb} shell am display-size reset && ${adb} shell input keyevent KEYCODE_APP_SWITCH`;

            exec(enterSplitScreen, { timeout: 8000 }, (err2) => {
                setTimeout(() => {
                    // Step 3: 通过长按recent触发分屏（模拟用户操作）
                    // 更可靠的方式：使用 am stack 命令（需要root或系统权限）
                    // 通用方式：先发送recent键，再长按
                    const triggerSplit = `${adb} shell input keyevent --longpress KEYCODE_APP_SWITCH`;

                    exec(triggerSplit, { timeout: 8000 }, (err3) => {
                        setTimeout(() => {
                            // Step 4: 启动第二个app到另一个分屏区域
                            const launchApp2 = `${adb} shell monkey -p ${app2} -c android.intent.category.LAUNCHER 1`;

                            exec(launchApp2, { timeout: 10000 }, (err4) => {
                                if (err4) {
                                    return res.json({ success: false, message: `启动 ${app2} 失败: ${err4.message}` });
                                }
                                res.json({
                                    success: true,
                                    message: `已尝试分屏启动 ${app1} 和 ${app2}。如未生效，请在手机上手动拖拽到分屏区域。`
                                });
                            });
                        }, 1500);
                    });
                }, 1000);
            });
        }, 2000);
    });
});

// ADB 分屏（Android 9+ 推荐方式，使用 am start-activity 的 --windowingMode 参数）
app.post('/api/adb/splitscreen2', (req, res) => {
    const { deviceId, app1, app2 } = req.body;
    if (!deviceId || !app1 || !app2) {
        return res.json({ success: false, message: '参数缺失' });
    }

    const adbPath = path.join(__dirname, '..', 'tools', 'adb', 'adb.exe');
    const adb = `"${adbPath}" -s ${deviceId}`;

    // 先查询app1的主Activity
    exec(`${adb} shell cmd package resolve-activity --brief ${app1} 2>/dev/null | tail -1`, { timeout: 8000 }, (err1, out1) => {
        const activity1 = out1.trim();

        exec(`${adb} shell cmd package resolve-activity --brief ${app2} 2>/dev/null | tail -1`, { timeout: 8000 }, (err2, out2) => {
            const activity2 = out2.trim();

            // --windowingMode 3 = 分屏主区域 (WINDOWING_MODE_SPLIT_SCREEN_PRIMARY)
            // --windowingMode 4 = 分屏副区域 (WINDOWING_MODE_SPLIT_SCREEN_SECONDARY)
            const cmd1 = activity1
                ? `${adb} shell am start --windowingMode 3 -n ${activity1}`
                : `${adb} shell monkey -p ${app1} -c android.intent.category.LAUNCHER 1`;

            exec(cmd1, { timeout: 10000 }, (e1) => {
                setTimeout(() => {
                    const cmd2 = activity2
                        ? `${adb} shell am start --windowingMode 4 -n ${activity2}`
                        : `${adb} shell monkey -p ${app2} -c android.intent.category.LAUNCHER 1`;

                    exec(cmd2, { timeout: 10000 }, (e2) => {
                        res.json({
                            success: !e1 && !e2,
                            message: (!e1 && !e2)
                                ? `分屏启动成功: ${app1} | ${app2}`
                                : `部分失败 - app1: ${e1 ? e1.message : 'OK'}, app2: ${e2 ? e2.message : 'OK'}`
                        });
                    });
                }, 2000);
            });
        });
    });
});

// ADB 投屏（scrcpy）
const scrcpyProcesses = {};

app.post('/api/adb/casting', (req, res) => {
    const { deviceId } = req.body;
    if (!deviceId) {
        return res.json({ success: false, message: '参数缺失: deviceId 必填' });
    }

    const scrcpyPath = path.join(__dirname, '..', 'tools', 'scrcpy', 'scrcpy.exe');
    const cmd = `"${scrcpyPath}" -s ${deviceId} --window-title "投屏-${deviceId}"`;

    const child = require('child_process').spawn(cmd, [], { shell: true, detached: true });
    scrcpyProcesses[deviceId] = child;

    child.on('error', (err) => {
        console.error(`scrcpy error for ${deviceId}:`, err);
    });

    res.json({ success: true, message: `已启动投屏: ${deviceId}` });
});

// 关闭投屏
app.post('/api/adb/stop_casting', (req, res) => {
    const { deviceId } = req.body;
    if (!deviceId) {
        return res.json({ success: false, message: '参数缺失: deviceId 必填' });
    }

    const child = scrcpyProcesses[deviceId];
    if (child) {
        try {
            process.kill(-child.pid);
        } catch (e) {
            // 如果进程组kill失败，尝试直接kill
            try { child.kill(); } catch (e2) {}
        }
        delete scrcpyProcesses[deviceId];
        return res.json({ success: true, message: `已关闭投屏: ${deviceId}` });
    }

    // 也尝试用taskkill关闭所有scrcpy进程（按窗口标题过滤）
    const { exec } = require('child_process');
    exec(`taskkill /FI "WINDOWTITLE eq 投屏-${deviceId}" /F`, (err, stdout) => {
        if (err) {
            return res.json({ success: false, message: `关闭投屏失败: ${err.message}` });
        }
        res.json({ success: true, message: `已关闭投屏: ${deviceId}` });
    });
});

// 关闭所有投屏
app.post('/api/adb/stop_all_casting', (req, res) => {
    const ids = Object.keys(scrcpyProcesses);
    ids.forEach(deviceId => {
        const child = scrcpyProcesses[deviceId];
        if (child) {
            try { process.kill(-child.pid); } catch (e) {
                try { child.kill(); } catch (e2) {}
            }
            delete scrcpyProcesses[deviceId];
        }
    });
    // 同时强制关闭所有 scrcpy 进程
    exec('taskkill /IM scrcpy.exe /F', (err) => {
        res.json({ success: true, message: `已关闭所有投屏，共 ${ids.length} 个` });
    });
});

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});
