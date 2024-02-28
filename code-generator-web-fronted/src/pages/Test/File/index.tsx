import { COS_HOST } from '@/constants';
import {testDownLoadFileUsingGet, testUploadFileUsingPost} from '@/services/backend/fileController';
import { InboxOutlined } from '@ant-design/icons';
import {Button, Card, Divider, Flex, message, Upload, UploadProps} from 'antd';
import React, { useState } from 'react';
import {saveAs} from "file-saver";

const { Dragger } = Upload;

/**
 * 文件上传下载测试页面
 * @constructor
 */
const TestFilePage: React.FC = () => {
  const [value, setValue] = useState<String>();

  const props: UploadProps = {
    name: 'file',
    //是否允许上传多个文件
    multiple: false,
    //用组件最多上传的次数
    maxCount: 1,
    customRequest: async (fileObj: any) => {
      try {
        const res = await testUploadFileUsingPost({}, fileObj.file);
        //onSuccess告诉组件什么时候成功展示
        fileObj.onSuccess(res.data);
        setValue(res.data);
      } catch (e: any) {
        message.error('上传失败，' + e.message);
        fileObj.onError(e);
      }
    },
    onRemove(file) {
      setValue(undefined);
    },
  };

  return (
    <Flex gap={16}>
      <Card title="文件上传">
        <Dragger {...props}>
          <p className="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p className="ant-upload-text">Click or drag file to this area to upload</p>
          <p className="ant-upload-hint">
            Support for a single or bulk upload. Strictly prohibited from uploading company data or
            other banned files.
          </p>
        </Dragger>
      </Card>
      <Card title="文件下载">
        <div>文件地址:{COS_HOST + value}</div>
        <Divider />
        <img src={COS_HOST + value} height={200} />
        <Divider />
        <Button onClick={ async()=>{
          const blob = await testDownLoadFileUsingGet(
      {
              filePath: value
              },
      {
            responseType: 'blob',
            },
          );
          //使用file-saver 下载文件, 定义fullPath防止用户直接输入.png
          const fullPath = COS_HOST + value;
          //从路径的最后一个斜杠的下一个字符开始取
          saveAs(blob,fullPath.substring(fullPath.lastIndexOf("/")+1));
        }}
        >
          点击下载文件</Button>
      </Card>
    </Flex>
  );
};

export default TestFilePage;
