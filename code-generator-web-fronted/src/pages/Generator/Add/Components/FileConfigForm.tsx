import { CloseOutlined } from '@ant-design/icons';
import { Button, Card, Form, FormListFieldData, Input, Select, Space } from 'antd';

interface Props {
  formRef: any;
  oldData: any;
}

export default (props: Props) => {
  const { formRef, oldData } = props;

  /**
   * 内层单个表单填写视图
   *
   * @param field
   * @param remove
   */

  const singleFiledFormView = (
    field: FormListFieldData,
    remove?: (index: number | number[]) => void,
  ) => (
    //未分组文件
    <Space>
      <Form.Item label="输入路径" name={[field.name, 'inputPath']}>
        <Input />
      </Form.Item>
      <Form.Item label="输出路径" name={[field.name, 'outputPath']}>
        <Input />
      </Form.Item>
      <Form.Item label="类型" name={[field.name, 'type']}>
        <Select
          style={{
            minWidth: 80,
          }}
          options={[
            {
              value: 'file',
              label: '文件',
            },
            {
              value: 'dir',
              label: '目录',
            },
          ]}
        />
      </Form.Item>
      <Form.Item label="生成类型" name={[field.name, 'generateType']}>
        <Select
          style={{
            minWidth: 80,
          }}
          options={[
            {
              value: 'static',
              label: '静态',
            },
            {
              value: 'dynamic',
              label: '动态',
            },
          ]}
        />
      </Form.Item>
      <Form.Item label="条件" name={[field.name, 'condition']}>
        <Input />
      </Form.Item>
      {remove && (
        <Button type="text" danger onClick={() => remove(field.name)}>
          删除
        </Button>
      )}
    </Space>
  );

  return (
    <Form.List name={['fileConfig', 'files']}>
      {(fields, { add, remove }) => (
        <div style={{ display: 'flex', rowGap: 16, flexDirection: 'column' }}>
          {fields.map((field) => {
            //注意要获取groupKey,防止修改时识别分组错误
            const fileConfig =
              formRef?.current?.getFieldsValue()?.fileConfig ?? oldData?.fileConfig;
            const groupKey = fileConfig.files?.[field.name]?.groupKey;

            return (
              <Card
                size="small"
                title={groupKey ? '分组字段' : '未分组字段'}
                key={field.key}
                extra={
                  <CloseOutlined
                    onClick={() => {
                      remove(field.name);
                    }}
                  />
                }
              >
                {/*对于分组文件*/}
                {groupKey ? (
                  <>
                    <Space>
                      <Form.Item label="分组key" name={[field.name, 'groupKey']}>
                        <Input />
                      </Form.Item>
                      <Form.Item label="组名" name={[field.name, 'groupName']}>
                        <Input />
                      </Form.Item>
                      <Form.Item label="条件" name={[field.name, 'condition']}>
                        <Input />
                      </Form.Item>
                    </Space>
                  </>
                ) : (
                  singleFiledFormView(field)
                )}

                {/*组内字段 ,如果groupKey存在再渲染组内字段*/}
                {groupKey && (
                  <Form.Item label="组内字段">
                    <Form.List name={[field.name, 'files']}>
                      {(subFields, subOpt) => (
                        <div style={{ display: 'flex', flexDirection: 'column', rowGap: 16 }}>
                          {subFields.map((subField) => {
                            return singleFiledFormView(subField, subOpt.remove);
                          })}
                          <Button type="dashed" onClick={() => subOpt.add()}>
                            添加组内字段
                          </Button>
                        </div>
                      )}
                    </Form.List>
                  </Form.Item>
                )}
              </Card>
            );
          })}

          <Button type="dashed" onClick={() => add()}>
            添加字段
          </Button>

          <Button
            type="dashed"
            onClick={() =>
              add({
                groupKey: 'groupKey',
                groupName: '分组',
                type: 'group'
              })
            }
            block
          >
            添加分组
          </Button>
          <div style={{ marginBottom: 16 }} />
        </div>
      )}
    </Form.List>
  );
};
