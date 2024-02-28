import { Avatar, Card } from 'antd';
import React from 'react';

//加载外层接收的代码生成器的参数
interface Props {
  data: API.GeneratorVO;
}

/**
 * 作者信息
 * @param props
 * @constructor
 */
const AuthorInfo: React.FC<Props> = (props) => {
  //从props中取出data
  const { data } = props;

  const user = data?.user;
  if (!user) {
    return <></>;
  }
  return (
    <div style={{ marginTop: 16 }}>
      <Card.Meta
        title={user.userName}
        description={user.userProfile}
        avatar={<Avatar size={64} src={user.userAvatar} />}
      />
    </div>
  );
};

export default AuthorInfo;
