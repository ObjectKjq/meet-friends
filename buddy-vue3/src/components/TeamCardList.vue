<template>
  <div id="teamCardList">
    <van-card
        v-for="(team, index) in props.teamList"
        :thumb="duiwu"
        :desc="team.description"
        :title="`${team.name}`"
    >
      <template #tags>
        <van-tag plain type="danger" style="margin-right: 8px; margin-top: 8px">
          {{
            teamStatusEnum[team.status]
          }}
        </van-tag>
      </template>
      <template #bottom>
        <div>
          {{ `队伍人数: ${team.hasJoinNum}/${team.maxNum}` }}
        </div>
        <div v-if="team.expireTime">
          {{ '过期时间: ' + UTCTime(team.expireTime) }}
        </div>
        <div>
          {{ '创建时间: ' + UTCTime(team.createTime) }}
        </div>
      </template>
      <template #footer>
        <van-button size="small" type="primary" v-if="team.userId !== currentUser?.id && !team.hasJoin" plain
                    @click="preJoinTeam(team)">
          加入队伍
        </van-button>
        <van-button v-if="team.userId === currentUser?.id" size="small" plain
                    @click="doUpdateTeam(team.id)">更新队伍
        </van-button>
        <!-- 仅加入队伍可见 -->
        <van-button v-if="team.userId !== currentUser?.id && team.hasJoin" size="small" plain
                    @click="doQuitTeam(team.id)">退出队伍
        </van-button>
        <van-button v-if="team.userId === currentUser?.id" size="small" type="danger" plain
                    @click="doDeleteTeam(team.id, index)">解散队伍
        </van-button>
      </template>
    </van-card>
    <van-dialog v-model:show="showPasswordDialog" title="请输入密码" show-cancel-button @confirm="doJoinTeam" @cancel="doJoinCancel">
      <van-field v-model="password" placeholder="请输入密码"/>
    </van-dialog>
  </div>
</template>

<script setup lang="ts">
import {teamStatusEnum} from "../constants/team";
import {TeamType} from "../models/team";
import {UTCTime} from '../utils/UTCTime'
import duiwu from '../assets/duiwu.png';
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import {getLoginUser, joinTeam, quitTeam, deleteTeam} from '../api/tag'
import { showSuccessToast } from 'vant';

interface TeamCardListProps {
  teamList: TeamType[];
}

const props = withDefaults(defineProps<TeamCardListProps>(), {
  // @ts-ignore
  teamList: [] as TeamType[],
});

// 密码的输入框是否显示
const showPasswordDialog = ref(false);
// 用户输入的密码
const password = ref('');
// 要加入队伍的id
const joinTeamId = ref(0);
// 获取当前登录用户
const currentUser = ref();

const router = useRouter();

// 获取当前用户信息
onMounted(async () => {
  const res = await getLoginUser();
  currentUser.value = res.data;
})

// 加入队伍
const preJoinTeam = (team: TeamType) => {
  joinTeamId.value = team.id;
  if (team.status === 0) {
    doJoinTeam()
  } else {
    showPasswordDialog.value = true;
  }
}

// 取消输入密码，还原状态
const doJoinCancel = () => {
  joinTeamId.value = 0;
  password.value = '';
}

/**
 * 加入队伍
 */
const doJoinTeam = async () => {
  if (!joinTeamId.value) {
    return;
  }
  const res = await joinTeam({
    teamId: joinTeamId.value,
    password: password.value
  })
  if(res.code === 20000){
    const team = props.teamList.find(team=>{
      return team.id === joinTeamId.value;
    })
    //修改加入成功
    team.hasJoin = true;
    showSuccessToast("加入成功!")
  }
}

/**
 * 跳转至更新队伍页
 * @param id
 */
const doUpdateTeam = (id: number) => {
  router.push({
    path: '/team/update',
    query: {
      id,
    }
  })
}

/**
 * 退出队伍
 * @param id
 */
const doQuitTeam = async (id: number) => {
  const res = await quitTeam(id);
  if (res.code === 20000) {
    const team = props.teamList.find(team=>{
      return team.id === id;
    })
    //修改加入成功
    team.hasJoin = false;
    showSuccessToast('操作成功');
  }
}

/**
 * 解散队伍
 * @param id
 */
const doDeleteTeam = async (id: number, index) => {
  const res = await deleteTeam(id);
  if (res.code === 20000) {
    props.teamList.splice(index, 1)
    showSuccessToast('操作成功');
  } 
}

</script>

<style scoped>
#teamCardList :deep(.van-image__img) {
  height: 128px;
  object-fit: unset;
}
</style>
