import FileUploader from '@/components/FileUploader';
import PictureUploader from '@/components/PictureUploader';
import { COS_HOST } from '@/constants';
import FileConfigForm from '@/pages/Generator/Add/Components/FileConfigForm';
import GeneratorMaker from '@/pages/Generator/Add/Components/GeneratorMaker';
import ModelConfigForm from '@/pages/Generator/Add/Components/ModelConfigForm';
import {
  addGeneratorUsingPost,
  editGeneratorUsingPost,
  getGeneratorVoByIdUsingGet,
} from '@/services/backend/generatorController';
import { useSearchParams } from '@@/exports';
import {
  ProCard,
  ProFormInstance,
  ProFormItem,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import { history } from '@umijs/max';
import { Alert, message, UploadFile } from 'antd';
import React, { useEffect, useRef, useState } from 'react';

/**
 * 生成器创建页面
 * @constructor
 */
const GeneratorAddPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const id = searchParams.get('id');
  const [oldData, setOldData] = useState<API.GeneratorEditRequest>();
  const formRef = useRef<ProFormInstance>();
  //记录表单已填数据
  const [basicInfo, setBasicInfo] = useState<API.GeneratorEditRequest>();
  const [modelConfig, setModelConfig] = useState<API.ModelConfig>();
  const [fileConfig, setFileConfig] = useState<API.FileConfig>();

  /**
   * 加载数据
   */
  const loadData = async () => {
    if (!id) {
      return;
    }
    try {
      const res = await getGeneratorVoByIdUsingGet({
        // @ts-ignore
        id,
      });
      //之前上传时是对象转换成路径字符串，现在得是字符串转换成对象
      if (res.data) {
        const { distPath } = res.data ?? {};
        if (distPath) {
          //@ts-ignore
          res.data.distPath = [
            {
              uid: id,
              name: '文件' + id,
              status: 'done',
              url: COS_HOST + distPath,
              response: distPath,
            } as UploadFile,
          ];
        }
        //setOldData是异步执行的，执行 formRef.current?.setFieldsValue(oldData);时oldData为空
        setOldData(res.data);
      }
    } catch (error: any) {
      message.error('加载数据失败,' + error.message);
    }
  };

  //什么时候调用函数,用useEffect指定
  useEffect(() => {
    if (id) {
      loadData();
    }
  }, [id]);

  /**
   * 创建
   * @param values
   */
  const doAdd = async (values: API.GeneratorAddRequest) => {
    //调用接口
    try {
      const res = await addGeneratorUsingPost(values);
      if (res.data) {
        message.success('创建成功');
        history.push(`/generator/detail/${res.data}`);
      }
    } catch (error: any) {
      message.error('创建失败' + error.message);
    }
  };

  /**
   * 更新
   * @param values
   */
  const doUpdate = async (values: API.GeneratorEditRequest) => {
    //调用接口
    try {
      const res = await editGeneratorUsingPost(values);
      if (res.data) {
        message.success('更新成功');
        history.push(`/generator/detail/${id}`);
      }
    } catch (error: any) {
      message.error('更新失败' + error.message);
    }
  };

  /**
   * 提交
   * @param values
   */
  const doSubmit = async (values: API.GeneratorAddRequest) => {
    //对数据进行转化
    if (!values.fileConfig) {
      values.fileConfig = {};
    }
    if (!values.modelConfig) {
      values.modelConfig = {};
    }
    //将dist文件列表改成url
    if (values.distPath && values.distPath.length > 0) {
      //@ts-ignore
      values.distPath = values.distPath[0].response;
    }

    //调用接口
    if (id) {
      await doUpdate({
        // @ts-ignore
        id,
        ...values,
      });
    } else {
      await doAdd(values);
    }
  };


  return (
    <>
      <Alert message="如果不需要在线制作功能，可不需要填写" type="warning" closable />
      <div style={{ marginBottom: 16 }} />
      <ProCard>
        {/*创建或已加载要更新的数据时，才渲染表单，顺利填充默认值*/}
        {(!id || oldData) && (
          <StepsForm<API.GeneratorAddRequest | API.GeneratorEditRequest>
            formRef={formRef}
            formProps={{
              initialValues: oldData,
            }}
            onFinish={doSubmit}
          >
            <StepsForm.StepForm
              name="base"
              title="基本信息"
              onFinish={async (values) => {
                setBasicInfo(values);
                return true;
              }}
            >
              <ProFormText name="name" label="名称" placeholder="请输入名称" />
              <ProFormTextArea name="description" label="描述" placeholder="请输入描述" />
              <ProFormText name="author" label="作者" placeholder="请输入作者名称" />
              <ProFormText name="version" label="版本" placeholder="请输入版本号" />
              <ProFormSelect label="标签" mode="tags" name="tags" placeholder="请输入标签列表" />
              <ProFormItem label="图片" name="picture">
                <PictureUploader biz="generator_picture" />
              </ProFormItem>
            </StepsForm.StepForm>

            <StepsForm.StepForm
              name="modelCongig"
              title="模型配置"
              onFinish={async (values) => {
                setModelConfig(values);
                return true;
              }}
            >
              <ModelConfigForm formRef={formRef} oldData={oldData} />
            </StepsForm.StepForm>
            <StepsForm.StepForm
              name="fileConfig"
              title="文件配置"
              onFinish={async (values) => {
                setFileConfig(values);
                return true;
              }}
            >
              <FileConfigForm formRef={formRef} oldData={oldData} />
            </StepsForm.StepForm>
            <StepsForm.StepForm name="dist" title="生成器文件">
              <ProFormItem label="产物包" name="distPath">
                <FileUploader biz="generator_dist" description="请上传生成器文件压缩包" />
              </ProFormItem>

              <GeneratorMaker meta={{
                  ...basicInfo,
                  ...modelConfig,
                  ...fileConfig,
                }}/>
            </StepsForm.StepForm>
          </StepsForm>
        )}
      </ProCard>
    </>
  );
};
export default GeneratorAddPage;
