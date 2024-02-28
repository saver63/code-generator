import { COS_HOST } from '@/constants';
import {
  testDownLoadFileUsingGet,
  testUploadFileUsingPost,
  uploadFileUsingPost
} from '@/services/backend/fileController';
import { InboxOutlined } from '@ant-design/icons';
import {Button, Card, Divider, Flex, message, Upload, UploadFile, UploadProps} from 'antd';
import React, { useState } from 'react';
import {saveAs} from "file-saver";

const { Dragger } = Upload;

//让父级改变子组件的值
interface Props{
  biz: string,
  onChange?: (fileList: UploadFile[])=> void;
  value?: UploadFile[];
  description?: string;
}

/**
 * 文件上传组件
 * @constructor
 */
const FileUploader: React.FC<Props> = (props) => {
  const {biz,value,description,onChange} = props;
  const [loading,setloading] = useState<boolean>(false);

  const uploadProps: UploadProps = {
    name: 'file',
    //是否允许上传多个文件
    multiple: false,
    //用组件最多上传的次数
    maxCount: 1,
    //展示文件列表的方式
    listType: "text",
    //外层接收fileList，并作为参数
    fileList: value,
    //如果loading为true就禁用
    disabled: loading,
    //第一种改变onChange的方式:接收外层的onChange传入的参数，调用外层的属性onChange 把子组件的fileList传给外层的onChange
    onChange({ fileList} ) {
      onChange?.(fileList);
    },
    customRequest: async (fileObj: any) => {
      setloading(true);
      try {
        const res = await uploadFileUsingPost(
          {
            biz,
          },
          {},
          fileObj.file,
        );
        //onSuccess告诉组件什么时候成功展示
        fileObj.onSuccess(res.data);
      } catch (e: any) {
        message.error('上传失败，' + e.message);
        fileObj.onError(e);
      }
      setloading(false);
    },
  };

  return (
    <Dragger {...uploadProps}>
      <p className="ant-upload-drag-icon">
        <InboxOutlined />
      </p>
      <p className="ant-upload-text">点击或拖拽文件上传</p>
      <p className="ant-upload-hint">{description}</p>
    </Dragger>
  );
};

export default FileUploader;
