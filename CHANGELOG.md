# Changelog

## [0.7.6](https://github.com/wahidrizka/todolist/compare/v0.7.5...v0.7.6) (2025-09-26)


### Bug Fixes

* **api-docs:** document error/edge responses for legacy todos controller ([a63a9ab](https://github.com/wahidrizka/todolist/commit/a63a9ab6e334a69083a134b955ea591495b629a6))
* **api-docs:** document error/edge responses for legacy todos controller ([5a9352b](https://github.com/wahidrizka/todolist/commit/5a9352b6ce3546d0554ea0a0979bcbfb95f8732d))

## [0.7.5](https://github.com/wahidrizka/todolist/compare/v0.7.4...v0.7.5) (2025-09-26)


### Bug Fixes

* **db:** wrap JDBC write operations in transactions ([1561d6e](https://github.com/wahidrizka/todolist/commit/1561d6ed9a2def012d659cda64056c781aa39277))
* **db:** wrap JDBC write operations in transactions ([6dda74a](https://github.com/wahidrizka/todolist/commit/6dda74a15991f75fa9ea351310f0ad9979f4885a))

## [0.7.4](https://github.com/wahidrizka/todolist/compare/v0.7.3...v0.7.4) (2025-09-25)


### Bug Fixes

* **ci:** remove nested 'script:' in deploy workflow SSH step ([f094dbc](https://github.com/wahidrizka/todolist/commit/f094dbc1931ee83a986f02385f601e5098d56ffe))
* **ci:** remove nested 'script:' in deploy workflow SSH step ([0edd984](https://github.com/wahidrizka/todolist/commit/0edd984136edd6594c7bdff0e723b384b5437ebd))

## [0.7.3](https://github.com/wahidrizka/todolist/compare/v0.7.2...v0.7.3) (2025-09-25)


### Bug Fixes

* **deploy:** strip 'v' prefix from release tag before pulling GHCR image ([1ed9602](https://github.com/wahidrizka/todolist/commit/1ed96025329ac7f067cb855306434649ca59a891))
* **deploy:** strip 'v' prefix from release tag before pulling GHCR image ([aa197ab](https://github.com/wahidrizka/todolist/commit/aa197abf7d3ce58bf43c95dc3dcf22a98c7bcd46))

## [0.7.2](https://github.com/wahidrizka/todolist/compare/v0.7.1...v0.7.2) (2025-09-25)


### Bug Fixes

* **release:** align release-please baseline to 0.7.1 and strip 'v' prefix in publish workflow ([4b5f299](https://github.com/wahidrizka/todolist/commit/4b5f299d9358b0fecb64dafbcf38116ea44e3694))
* **release:** align release-please baseline to 0.7.1 and strip 'v' prefix in publish workflow ([6bc296c](https://github.com/wahidrizka/todolist/commit/6bc296cb7579fdb680f1cc11951bae56c5cf2594))

## [0.7.1](https://github.com/wahidrizka/todolist/compare/v0.7.0...v0.7.1) (2025-09-25)


### Bug Fixes

* **build:** sync actuator build.version with release tag via ${revision} ([145a541](https://github.com/wahidrizka/todolist/commit/145a541350f868c90635e526a396bd0fc2fb02bd))
* **todo:** trim title on create in InMemoryTodoService to mirror PATCH behavior ([59ca9a4](https://github.com/wahidrizka/todolist/commit/59ca9a4ec4004664fc3cbde1985eb5b123171942))

## [0.7.0](https://github.com/wahidrizka/todolist/compare/v0.6.0...v0.7.0) (2025-09-21)


### Features

* **todo:** add pageable/filtering GET + implement services + bean validation and global handler ([eeac36a](https://github.com/wahidrizka/todolist/commit/eeac36abbd01a4d8c11348941a1f6e37321ef4c9))
* **todo:** add pageable/filtering GET + implement services + bean validation and global handler ([70078b2](https://github.com/wahidrizka/todolist/commit/70078b2cc1fdbedc4ccafcfd50276dae6d8dac33))

## [0.6.0](https://github.com/wahidrizka/todolist/compare/v0.5.0...v0.6.0) (2025-09-21)


### Features

* **todo:** add PATCH /api/todos/{id} to update title/completed ([25272e6](https://github.com/wahidrizka/todolist/commit/25272e6ab53f0ed9a2153894675975daabd43be4))
* **todo:** support PATCH /api/todos/{id} (update title/completed) ([45a5400](https://github.com/wahidrizka/todolist/commit/45a54004112c7cfc18e91c470b78e25d387b343b))

## [0.5.0](https://github.com/wahidrizka/todolist/compare/v0.4.1...v0.5.0) (2025-09-21)


### Features

* **todo:** persist to Postgres in prod via JdbcTodoService; scope In… ([824754f](https://github.com/wahidrizka/todolist/commit/824754f0463e7a5f99a28a0174ed7995209db00e))
* **todo:** persist to Postgres in prod via JdbcTodoService; scope InMemory to not-prod ([cf9ac3f](https://github.com/wahidrizka/todolist/commit/cf9ac3f60b25f297105e89abb409c7273da200ad))

## [0.4.1](https://github.com/wahidrizka/todolist/compare/v0.4.0...v0.4.1) (2025-09-20)


### Bug Fixes

* **release:** e2e trigger via Release Please ([b081583](https://github.com/wahidrizka/todolist/commit/b081583ce3e6647d5e54127079f2db9a03a14d10))
* **release:** e2e trigger via Release Please ([dbc0285](https://github.com/wahidrizka/todolist/commit/dbc02855d04852480cc0414a9b824cb311c539b1))

## [0.4.0](https://github.com/wahidrizka/todolist/compare/v0.3.2...v0.4.0) (2025-09-20)


### Features

* **todo:** add DELETE /api/todos/{id} and implement service.delete ([f3dd620](https://github.com/wahidrizka/todolist/commit/f3dd620649d4b7423e4d735eebfaae718de0173c))
* **todo:** tambah DELETE /api/todos/{id} + perkuat WebMvc tests ([bbb2e36](https://github.com/wahidrizka/todolist/commit/bbb2e36016330f7fd5a985a768e7beff63cb29a7))

## [0.3.2](https://github.com/wahidrizka/todolist/compare/v0.3.1...v0.3.2) (2025-09-20)


### Bug Fixes

* **version:** mengubah & menjadi dan ([e3b56de](https://github.com/wahidrizka/todolist/commit/e3b56dedbd5e1e5b7d2afa570518eaab2605d6b7))
* **version:** mengubah & menjadi dan ([deec827](https://github.com/wahidrizka/todolist/commit/deec82719256404ad2f23c25def54d578e19ab70))

## [0.3.1](https://github.com/wahidrizka/todolist/compare/v0.3.0...v0.3.1) (2025-09-20)


### Bug Fixes

* **version:** perjelas deskripsi endpoint versi (tampilkan metadata build) ([ac0d125](https://github.com/wahidrizka/todolist/commit/ac0d1250f86a00f2dc635d02e9b139ac5c97d4a8))
* **version:** perjelas deskripsi endpoint versi (tampilkan metadata build) ([4529373](https://github.com/wahidrizka/todolist/commit/4529373efc7aceb5a2d73bfd2631f2597d39393f))

## [0.3.0](https://github.com/wahidrizka/todolist/compare/v0.2.1...v0.3.0) (2025-09-20)


### Features

* **todo:** add GET /api/todos/{id} endpoint and WebMvc tests ([5f578b6](https://github.com/wahidrizka/todolist/commit/5f578b6a84bec9984b4d05567b8958f6f2f3b2f2))
* **version:** add /api/version using BuildProperties + GitProperties ([58d43e5](https://github.com/wahidrizka/todolist/commit/58d43e5b80e176676f1c7de0af2d8d7155f02d93))
* **version:** add /api/version using BuildProperties + GitProperties ([26f4cf3](https://github.com/wahidrizka/todolist/commit/26f4cf320f0b15b1aa23f74971927aa25823e3e4))
* **version:** add /api/version using Spring Boot build-info ([f4a929a](https://github.com/wahidrizka/todolist/commit/f4a929a97ee7ccb9bcac76eab17132d839341af3))
* **version:** add /api/version using Spring Boot build-info ([75bc91a](https://github.com/wahidrizka/todolist/commit/75bc91a3a05f83cfc3b221d8b53ce6ba4d1b7a0e))


### Bug Fixes

* **db:** add Flyway PostgreSQL module to support PG 16.x in prod ([b70eb1b](https://github.com/wahidrizka/todolist/commit/b70eb1b8890484c2c9ac92b2c56a57fafb738695))
* **db:** add Flyway PostgreSQL module to support PG 16.x in prod ([fbfeff0](https://github.com/wahidrizka/todolist/commit/fbfeff0bd9af6b6db74f8a040e14493131a127f6))

## [0.2.0](https://github.com/wahidrizka/todolist/compare/v0.1.0...v0.2.0) (2025-09-19)


### Features

* **todo:** add GET /api/todos/{id} endpoint and WebMvc tests ([5f578b6](https://github.com/wahidrizka/todolist/commit/5f578b6a84bec9984b4d05567b8958f6f2f3b2f2))
