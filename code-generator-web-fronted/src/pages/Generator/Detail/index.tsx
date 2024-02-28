import { COS_HOST } from '@/constants';
import AuthorInfo from '@/pages/Generator/Detail/components/AuthorInfo';
import FileConfig from '@/pages/Generator/Detail/components/FileConfig';
import ModelConfig from '@/pages/Generator/Detail/components/ModelConfig';
import {
  downloadGeneratorByIdUsingGet,
  getGeneratorVoByIdUsingGet,
} from '@/services/backend/generatorController';
import { useParams } from '@@/exports';
import { useModel } from '@@/plugin-model';
import { DownloadOutlined, EditOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import { Button, Card, Col, Image, message, Row, Space, Tabs, Tag, Typography } from 'antd';
import { saveAs } from 'file-saver';
import moment from 'moment';
import React, { useEffect, useState } from 'react';
import {Link} from "umi";

/**
 * 生成器详情页
 * @constructor
 */
const GeneratorDetailPage: React.FC = () => {
  const { id } = useParams();
  const [loading, setloading] = useState<boolean>(true);
  const [data, setData] = useState<API.GeneratorVO>({});
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState ?? {};
  //判断用户id是否是当前登录用户的创建者
  const my = data?.userId === currentUser?.id;

  /**
   * 加载数据
   */
  const loadData = async () => {
    if (!id) {
      return;
    }
    setloading(true);
    try {
      const res = await getGeneratorVoByIdUsingGet({
        id,
      });
      setData(res.data ?? {});
    } catch (error: any) {
      message.error('获取数据失败,' + error.message);
    }
    setloading(false);
  };

  //什么时候调用函数,用useEffect指定
  useEffect(() => {
    if (id) {
      loadData();
    }
  }, [id]);

  /**
   * 标签列表
   * @param tags
   */
  const tagListView = (tags?: string[]) => {
    if (!tags) {
      return <></>;
    }

    return (
      <div style={{ marginBottom: 8 }}>
        {tags.map((tag) => (
          <Tag key={tag}>{tag}</Tag>
        ))}
      </div>
    );
  };

  /**
   * 下载按钮
   */
  //判断用户是否上传生成器跑&&用户是否登录才去展示下载按钮
  const downloadButton = data.distPath && currentUser && (
    <Button
      icon={<DownloadOutlined />}
      onClick={async () => {
        const blob = await downloadGeneratorByIdUsingGet(
          { id },
          {
            responseType: 'blob',
          },
        );
        //使用file-saver 下载文件, 定义fullPath防止用户直接输入.png
        const fullPath = COS_HOST + data.distPath;
        //从路径的最后一个斜杠的下一个字符开始取
        saveAs(blob, fullPath.substring(fullPath.lastIndexOf('/') + 1));
      }}
    >
      下载
    </Button>
  );


  /**
   * 编辑按钮
   */
  const editButton =my &&(
    <Link to={`/generator/update?id=${data.id}`}>
      <Button icon={<EditOutlined />}>编辑</Button>
    </Link>
  )



  return (
    <PageContainer title={<></>} loading={loading}>
      <Card>
        <Row justify="space-between" gutter={[32, 32]}>
          <Col flex="auto">
            <Space size="large" align="center">
              <Typography.Title level={4}>{data.name} </Typography.Title>
              {tagListView(data.tags)}
            </Space>
            <Typography.Paragraph>{data.description}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">
              创建时间：{moment(data.createTime).format('YYYY-MM-DD hh:mm:ss')}
            </Typography.Paragraph>
            <Typography.Paragraph type="secondary">基础包：{data.basePackage}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">版本：{data.version}</Typography.Paragraph>
            <Typography.Paragraph type="secondary">作者：{data.author}</Typography.Paragraph>
            <div style={{ marginBottom: 24 }} />
            <Space size="middle">
              <Button type="primary">立即使用</Button>
              {downloadButton}
              {editButton}
            </Space>
          </Col>
          <Col flex="320px">
            <Image src={data.picture} />
          </Col>
        </Row>
      </Card>
      <div style={{ marginBottom: 24 }} />
      <Card>
        <Tabs
          size="large"
          defaultActiveKey={'FileConfig'}
          onChange={() => {}}
          items={[
            {
              key: 'fileConfig',
              label: '文件配置',
              children: <FileConfig data={data} />,
            },
            {
              key: 'modelConfig',
              label: '模型配置',
              children: <ModelConfig data={data} />,
            },
            {
              key: 'userInfo',
              label: '作者信息',
              children: <AuthorInfo data={data} />,
            },
          ]}
        />
      </Card>
    </PageContainer>
  );
};
export default GeneratorDetailPage;
