## ***es 1.0 API***
 
### 简述
>提供elasticsearch功能，透明化elasticsearch  

### 常用类

---
>##### 1. BaseCoreTypes
>指定field的type类型  
>
| Key       |   type     |    example    |
| :-------: |:--------   |:-------        |
| String    | 字符串      |"abc"    |
| Integer   | 整形        |45|
| Long      | 长整形      | 123321L |
| Date      | 日期        | 2003-11-11 |
| Float     | 单精度浮点数 | 123.456F |
| Double    | 双精度浮点数 | 123.456D |
| Boolean   | 布尔       | true/false |
| Object    | object    | 对象|
| Auto      | auto      |自动根据插入的值判断类型|
| Ip        | ip        |"192.168.1.125" |
| geo_point | 经纬度     |"32.009,120.890" 由纬度和经度用逗号分隔组成的字符串|


>##### 2. BaseCoreIndexs
>指定是否使用分词  
>
| Key          |  Resume|
| :-------:    |:--------   |
| not_analyzed | 否         |
| analyzed     | 是：默认，并使用elasticsearch自带的分词插件   |

>##### 3. BaseCoreStores
>指定是否存储
>
| Key      |   Resume  |
| :-------: |:--------  |
| TRUE      | 是        |
| FALSE     | 否：默认    |

>##### 4. BaseCoreDateFormats
>指定日期时间格式  
>
    简介略

>##### 5. BaseCoreIndexAnalyzers
>指定field使用的分词插件，在创建mapping的时候无需设置此项   
>需要设置分词插件只需BaseCoreIndexs选择analyzed  
>目前指定分词插件使用ik  
>该项用于getMapping时保存index_analyzer
>
| Name      |   Resume    |
| :-------: |:--------    |
| ik        | 使用ik分词插件 |


#### MappingTypes
>  createMapping或getMapping时的实体类  
>
| Name         |  type                |   Resume     |
| :-------:    |:-----                |:--------     |
| fieldName    | String               | field的name   |
| type         |BaseCoreTypes         | 类型          |
| index        |BaseCoreIndexs        | 是否使用分词插件|  
| store        |BaseCoreStores        | 是否存储      | 
| format       |BaseCoreDateFormats   | 日期时间类型   | 
| indexAnalyzer|BaseCoreIndexAnalyzers| 分词插件      |  
| nestedMappingTypes|List<MappingTypes>|有nested类型时使用|

---

#### InsertDataFormats
>  index或update的数据结构实体类
>
| Name         | Type     |   Resume                       |
| :-------:    |:-------- |:--------                       |
| field        |String    | field的name                    |
| fieldValue   |Object    | field的value                   |

---

>##### 1. QueryDataFormats.Greater
> 指定关系为大于或大于等于  
>
| Name      |   Resume  |
| :-------: |:--------  |
| gt        | 大于       |
| gte       | 大于等于    |

>##### 2. QueryDataFormats.Lesser
>指定关系为小于或小于等于
>
| Name      |   Resume  |
| :-------: |:--------  |
| lt        | 小于       |
| lte       | 小于等于    |

>##### 3. QueryDataFormats.RangeRelation
> 指定greater和lesser关系为and或or
>
| Name      |   Resume  |
| :-------: |:--------  |
| AND       | 与        |
| OR        | 或        |

>##### 4. QueryDataFormats.DataRelation
> 单条数据中的关系为and或or
>
| Name      |   Resume  |
| :-------: |:--------  |
| AND       | 与        |
| OR        | 或        |


#### QueryDataFormats
>  query或delete的数据结构实体类
>
| Name         | Type     |   Resume                       |
| :-------:    |:-------- |:--------                       |
| field        |String    | field的name                    |
| fieldValue   |Object    | field的value                   |
| greater      |Greater   | 关系为大于或大于等于              |  
| gValue       |Object    | 大于或大于等于的值                | 
| lesser       |Lesser    | 关系为小于或小于等于              | 
| lValue       |Object    | 小于或小于等于的值                |
| rangeRelation|RangeRelation| 范围数据的左右区间关系为and或or |
| dataRelation |DataRelation| 单条数据中的关系为and或or |
| lat          |Object    | 纬度值                          |
| lon          |Object    | 经度值                          |  
| from         |String    | 从point标点指定开始范围，如"0km"   | 
| to           |String    | 从point标点指定结束范围，如"100km" | 
| includeLower |boolean   | 是否包含开始范围                  |
| includeUpper |boolean   | 是否包含最大范围                  |



---

### 接口

UrlPrefix：  

    http://host:port/springes/

CommonMessages：

	SUCCESS(200,"Success"),
	ERROR(10001,"Unknow error"),
	NO_INDEX_DEFINE(10002, "No index defined"),
	NO_TYPE_DEFINE(10003, "No type defined"),
	NO_ID_DEFINE(10004,"No Id defined"),
	INDEX_EXISTS(10005,"Index is already exists"),
	NO_INDEX_EXISTS(10006, "No index exists"),
	NO_TYPE_EXISTS(10007, "No type exists"),
	NO_ID_EXISTS(10008,"No Id exists"),
	DATA_FORMAT_ERROR(10009,"Data format error"),
	NOT_EMPTY(10010," cannot be empty");


#### 1.创建Indices

> ##### 1.url
createIndices

> ##### 2.参数
>
| Name      |   Type   |   Mark   |
| :-------: |:--------:|:--------:|
| indices   | String   | 索引名字   |

> ##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |



#### 2.创建mapping

> ##### 1.url
createMapping

> ##### 2.参数
>
| Name      |   Type   |   Mark         |
| :-------: |:--------:|:--------:      |
| indexName | String   |  索引名字        |
| type      | String   |  索引类型           |
| fieldTypes| String   | List\<MappingTypes>.toString()|

>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |


#### 3.获取mapping

>##### 1.url
getMapping
>##### 2.描述
>
| Name      |   Type   |   Mark         |
| :-------: |:--------:|:--------:      |
| indexName | String   |  索引名字       |
| type      | String   |  索引类型       |

>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |
| data      | String   | Map.toString()  |


#### 4.插入一条数据

>##### 1.url
index
>##### 2.描述
>
| Name          |   Type   |   Mark         |
| :-------:     |:--------:|:--------:      |
| indexName     | String   |  索引名字       |
| type          | String   |  索引类型       |
|dataFormatsStr | String   | List\<InsertDataFormats>.toString()|

>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |


#### 5.批量插入数据

>##### 1.url
bulkIndex
>##### 2.描述
>
| Name          |   Type  |   Mark         |
| :-------:     |:-------:|:--------:      |
| indexName     | String  |  索引名字       |
| type          | String  |  索引类型       |
|jsonStr    | String  | List\<List\<InsertDataFormats>>.toString()|

>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |


#### 6.删除指定index,type,id的一条数据

>##### 1.url
delete
>##### 2.描述
>
| Name          |   Type   |   Mark         |
| :-------:     |:--------:|:--------:      |
| indexName     | String   |  索引名字       |
| type          | String   |  索引类型       |
| id            | String   |  索引id       |

>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |


#### 7.删除整个index

>##### 1.url
deleteIndex
>##### 2.描述
>
| Name          |   Type   |   Mark         |
| :-------:     |:--------:|:--------:      |
| indexName     | String   |  索引名字       |
>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |


#### 8.根据index，删除指定type

>##### 1.url
deleteType
>##### 2.描述
>
| Name          |   Type   |   Mark         |
| :-------:     |:--------:|:--------:      |
| indexName     | String   |  索引名字       |
| type          | String   |  索引类型       |
>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |


#### 9.删除符合jsonStr的数据

>#### 1.url
deleteByQuery
>##### 2.描述
>
| Name          |   Type   |   Mark         |
| :-------:     |:--------:|:--------:      |
| indexName     | String   |  索引名字       |
| type          | String   |  索引类型       |
| jsonStr       | String   |List\<QueryDataFormats>.toString()|
>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |


#### 10.根据index和id查找数据

>##### 1.url
queryById
>##### 2.描述
>
| Name          |   Type   |   Mark         |
| :-------:     |:--------:|:--------:      |
| indexName     | String   |  索引名字       |
| id            | String   |  索引id       |
>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |
| data      | String   | JSON.toString()|


#### 11.根据index查找符合jsonStr的数据

>##### 1.url
queryByI
>##### 2.描述
>
| Name      |   Type   |   Mark         |
| :-------: |:--------:|:--------:      |
| indexName | String   |  索引名字        |
| jsonStr   | String   |List\<QueryDataFormats>.toString()|
>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |
| data      | String   | List\<JSON>.toString()|


#### 12.根据index和type查找符合jsonStr的数据

>##### 1.url
queryByIT
>##### 2.描述
>
| Name      |   Type   |   Mark         |
| :-------: |:--------:|:--------:      |
| indexName | String   |  索引名字        |
| type      | String   |  索引类型        |
| jsonStr   | String   |List\<QueryDataFormats>.toString()|
>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |
| data      | String   | List\<JSON>.toString()|

#### 13.根据index查找符合jsonStr的数据，并指定PageBean分页信息

>##### 1.url
queryByIFS
>##### 2.描述
>
| Name      |   Type   |   Mark         |
| :-------: |:--------:|:--------:      |
| indexName | String   |  索引名字        |
| pageBean  | String   |PageBean.toString() |
| jsonStr   | String   |List\<QueryDataFormats>.toString()|
>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |
| data      | String   | PageBean.toString()|


#### 14.根据index和type查找符合jsonStr的数据，并指定PageBean分页信息

>##### 1.url
queryByITFS
>##### 2.描述
>
| Name      |   Type   |   Mark         |
| :-------: |:--------:|:--------:      |
| indexName | String   |  索引名字        |
| type      | String   |  索引类型        |
| pageBean  | String   |PageBean.toString() |
| jsonStr   | String   |List\<QueryDataFormats>.toString()|
>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |
| data      | String   | PageBean.toString()|


#### 15.更新指定id的数据

>##### 1.url
update
>##### 2.描述
>
| Name      |   Type   |   Mark         |
| :-------: |:--------:|:--------:      |
| indexName | String   |   索引名字       |
| type      | String   |   索引类型       |
| id        | String   |   索引id        |
| jsonStr   | String   |List\<InsertDataFormats>.toString()|
>##### 3.返回
>
| Name      |   Type   |   Mark        |
| :-------: |:--------:|:--------:     |
| status    | int      | 参考：200      |
| message   | String   | 消息:"Success" |

