import FileUploader from '@/components/FileUploader';
import { ProFormInstance, ProFormItem } from '@ant-design/pro-components';
import { ProForm } from '@ant-design/pro-form';
import {Collapse, message} from 'antd';
import { useRef } from 'react';
import {makeGeneratorUsingPost, useGeneratorUsingPost} from "@/services/backend/generatorController";
import {COS_HOST} from "@/constants";
import {saveAs} from "file-saver";

interface Props {
  meta: any;
}

export default (props: Props) => {
  const { meta } = props;
  const formRef = useRef<ProFormInstance>();

  /**
   * 提交
   * @param values
   */
  const doSubmit = async (values: API.GeneratorMakeRequest) => {
    //对数据进行转化
    if (!meta.name) {
      message.error("请填写名称");
      return;
    }

    //将dist文件列表改成url
    const zipFilePath=values.zipFilePath;
    if (!zipFilePath || zipFilePath.length < 1) {
      message.error("请上传模板文件压缩包");
      return;
    }
    //将模板文件转化成url
    //@ts-ignore
    values.zipFilePath = zipFilePath[0].response;

    try {
      const blob = await makeGeneratorUsingPost(
        {
          meta,
          zipFilePath:values.zipFilePath,
        },
        {
          responseType: 'blob',
        },
      );

      //从路径的最后一个斜杠的下一个字符开始取
      saveAs(blob, meta.name +".zip");
      }catch (error:any){
      message.error("制作失败"+error.message)
    }
  };





  const formView = (
    /**
     * 表单视图
     */
    <ProForm
      formRef={formRef}
      submitter={{
        searchConfig: {
          submitText: '制作',
        },
        resetButtonProps: {
          hidden: true,
        },
      }}
      onFinish={doSubmit}
    >
      <ProFormItem label="模板文件" name="zipFilePath">
        <FileUploader
          biz="generator_make_template"
          description="请上传压缩包,打包时不要添加最外层目录!"
        />
      </ProFormItem>
    </ProForm>
  );

  return (
    <Collapse
      style={{
        marginBottom:24,
      }}
      items={[
        {
          key: 'maker',
          label: '生成器制作工具',
          children: formView,
        },
      ]}
    />
  );
};
