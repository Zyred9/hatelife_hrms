# Ralph 教程

## Ralph 是什么

Ralph 是一个**自主 AI Agent 循环**，它会反复运行 AI 编程工具（Amp 或 Claude Code），每次使用全新上下文实例，直到 `prd.json` 中所有 User Story 都标记为完成。

核心思路：把一个大功能拆成多个小 Story，每次让 AI 只做一个 Story，跑完质量检查后提交，然后下一个 Story，如此循环。

- GitHub 地址: https://github.com/snarktank/ralph
- Stars: 14.8k+

---

## 核心架构

```
prd.json (Story 列表)
    ↓
ralph.sh (Bash 循环，每次启动一个全新的 Claude Code 实例)
    ↓
CLAUDE.md (给 AI 的指令：读 PRD → 选最高优先级 Story → 实现 → 跑检查 → 提交 → 更新状态)
    ↓
progress.txt (迭代间的记忆：经验教训、代码模式、踩坑记录)
    ↓
git history (代码变更历史)
```

**关键设计**：每次迭代 = 全新 AI 实例 = 干净上下文。迭代间只靠 git 历史、`progress.txt`、`prd.json` 传递信息。这避免了上下文膨胀导致 AI 质量下降。

---

## 安装步骤

### 前置条件

```bash
# 1. 确保 Claude Code 已安装并认证
npm install -g @anthropic-ai/claude-code

# 2. 确保 jq 已安装（macOS: brew install jq，Windows: choco install jq 或 scoop install jq）
jq --version

# 3. 你的项目必须是一个 git 仓库
git init  # 如果不是的话
```

### 方式一：复制到项目中（推荐）

```bash
# 在你的项目根目录
git clone https://github.com/snarktank/ralph.git --depth=1 temp-ralph
mkdir -p scripts/ralph
cp temp-ralph/ralph.sh scripts/ralph/
cp temp-ralph/CLAUDE.md scripts/ralph/
chmod +x scripts/ralph/ralph.sh
rm -rf temp-ralph
```

### 方式二：Claude Code Marketplace（技能全局安装）

```bash
# 在 Claude Code 中执行
/plugin marketplace add snarktank/ralph
/plugin install ralph-skills@ralph-marketplace
```

安装后你会获得两个技能：
- `/prd` — 生成需求文档
- `/ralph` — 把 PRD 转为 `prd.json` 格式

---

## 使用流程（三步）

### 1. 生成 PRD

在 Claude Code 中告诉它：

```
使用 prd 技能，为 [你的功能描述] 创建一个 PRD
```

它会回答一系列澄清问题，然后保存到 `tasks/prd-[feature-name].md`。

### 2. 转为 Ralph 格式

```
使用 ralph 技能，把 tasks/prd-[feature-name].md 转为 prd.json
```

生成 `prd.json`，内容示例：

```json
{
  "branchName": "ralph/add-user-profile-api",
  "userStories": [
    {
      "id": "US-001",
      "title": "创建用户资料数据库表",
      "description": "添加 t_user_profile 表...",
      "acceptanceCriteria": ["迁移脚本正确执行"],
      "passes": false
    },
    {
      "id": "US-002",
      "title": "实现用户资料 API 接口",
      "description": "...",
      "acceptanceCriteria": ["单元测试通过"],
      "passes": false
    }
  ]
}
```

### 3. 启动 Ralph

```bash
# 使用 Claude Code（默认 10 次迭代）
./scripts/ralph/ralph.sh --tool claude 10

# 不指定次数，默认 10 次
./scripts/ralph/ralph.sh --tool claude
```

每次循环 Ralph 会：
1. 检查/创建 `prd.json` 中定义的分支
2. 挑选最高优先级的 `passes: false` 的 Story
3. 实现该 Story
4. 运行质量检查（类型检查、测试、lint）
5. 通过则提交，提交信息格式：`feat: US-001 - 创建用户资料数据库表`
6. 更新 `prd.json` 标记该 Story `passes: true`
7. 追加经验到 `progress.txt`
8. 重复直到全部完成或达到最大迭代次数

---

## 关键要点

| 要点 | 说明 |
|------|------|
| **Story 要小** | 每个 Story 应该在一个上下文窗口内能完成，比如"添加一个字段"、"添加一个 API 接口" |
| **每次迭代干净上下文** | AI 不会记住上一轮对话，只通过 git + progress.txt + prd.json 传递信息 |
| **质量检查很重要** | 如果 typecheck/lint/test 失败，Ralph 不会提交脏代码 |
| **progress.txt 是记忆** | 每次迭代的经验教训会追加到这里，帮助后续迭代避免重复踩坑 |
| **CLAUDE.md 更新** | 发现可复用模式后，更新目录下的 CLAUDE.md，帮助未来开发者 |

---

## 调试命令

```bash
# 查看哪些 Story 完成了
cat prd.json | jq '.userStories[] | {id, title, passes}'

# 查看历史经验
cat progress.txt

# 查看 git 历史
git log --oneline -10
```

---

## 对 Java 项目的建议

如果你想用 Ralph 来驱动 Java 项目（如 `pg-open-jagat`）的开发：

1. **Story 粒度要合适**：Java 项目适合一个 Story 对应"一个接口"、"一个 Service 方法"、"一个实体类"
2. **质量检查换成 Maven**：把 typecheck/lint/test 换成 `mvn compile`、`mvn test`
3. **CLAUDE.md 需要自定义**：加入你项目的编码规范（你已有的 CLAUDE.md 已经非常完善了）
4. **自定义 prompt**：复制 `CLAUDE.md` 到项目后，修改质量检查命令为 `mvn compile -pl <module> -am` 等
