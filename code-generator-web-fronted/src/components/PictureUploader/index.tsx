import { COS_HOST } from '@/constants';
import {
  testDownLoadFileUsingGet,
  testUploadFileUsingPost,
  uploadFileUsingPost
} from '@/services/backend/fileController';
import {InboxOutlined, LoadingOutlined, PlusOutlined} from '@ant-design/icons';
import {Button, Card, Divider, Flex, message, Upload, UploadFile, UploadProps} from 'antd';
import React, { useState } from 'react';
import {saveAs} from "file-saver";
import * as url from "url";

const { Dragger } = Upload;

//2.让父级改变子组件的值
interface Props{
  biz: string,
  onChange?: (url: string)=> void;
  value?: string;
}

/**
 * 图片上传组件
 * @constructor
 */
const PictureUploader: React.FC<Props> = (props) => {
  const {biz,value,onChange} = props;
  const [loading,setloading] = useState<boolean>(false);

  //1.先触发
  const uploadProps: UploadProps = {
    name: 'file',
    //是否允许上传多个文件
    multiple: false,
    //用组件最多上传的次数
    maxCount: 1,
    //展示文件列表的方式
    listType: "picture-card",
    //如果loading为true就禁用
    disabled: loading,
    //隐藏文件上传列表
    showUploadList:false,
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
        //拼接完整图片路径
        const fullPath = COS_HOST + res.data;
        //第二种改变onChange的方式
        onChange?.(fullPath ?? '');
        //onSuccess告诉组件什么时候成功展示
        fileObj.onSuccess(res.data);
      } catch (e: any) {
        message.error('上传失败，' + e.message);
        fileObj.onError(e);
      }
      setloading(false);
    },
  };

  /**
   * 上传按钮
   */
  const uploadButton = (
    <button style={{ border: 0, background: 'none' }} type="button">
      {loading? <LoadingOutlined/> : <PlusOutlined />}
      <div style={{ marginTop: 8 }}>上传</div>
    </button>
  );

  //3.替换value值
  return (
    <Upload {...uploadProps}>
      {value ? <img src={value} alt="picture" style={{width: '100%'}} />: uploadButton}
    </Upload>
  );
};

export default PictureUploader;
