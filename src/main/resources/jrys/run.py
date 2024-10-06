import json

# 以二进制模式打开原始JSON文件
with open('D:\\01-自用\\java插件源码\\AronaBot\\src\\main\\resources\\jrys\\betterJrys.json', 'rb') as file:
    # 手动解码文件内容为JSON对象
    data = json.loads(file.read().decode('utf-8'))

# 提取含有"大吉"字眼的数据并按顺序重新编号
filtered_data = {}
count = 1
for key, value in data.items():
    for entry in value:
        if entry.get('fortuneSummary') and '大吉' in entry['fortuneSummary']:
            if str(count) not in filtered_data:
                filtered_data[str(count)] = []
            filtered_data[str(count)].append(entry)
            count += 1

# 将提取的数据写入新的JSON文件，使用UTF-8编码
with open('D:\\01-自用\\java插件源码\\AronaBot\\src\\main\\resources\\jrys\\filtered_data_daji_ordered.json', 'w', encoding='utf-8') as file:
    json.dump(filtered_data, file, indent=4, ensure_ascii=False)
