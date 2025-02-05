use forum_hub;

SELECT * FROM cursos;
INSERT INTO cursos(categoria, nome) VALUES ('PROGRAMACAO','Java e OO');

SELECT * FROM respostas;

SELECT * FROM topicos;

SELECT * FROM usuarios;
INSERT INTO usuarios(email, senha, nome_completo, nome_usuario) VALUES ('maria@email.com', '$2y$10$wXAjl/n68gkHX.hUZVXRS0O8gyfqp0meTbTzaiI559/qqIVxc2zbu', 'Maria', 'maria');
UPDATE usuarios SET senha='$2a$10$0grXirjs1lbx1Aq4feuemOq1ARnKzU3g4VVeJe.tJn3ILdUbHdS9a' WHERE id=13; 
DELETE FROM usuarios WHERE id=9;

SELECT * FROM perfis;
SELECT * FROM usuarios_perfis order by usuario_id asc;
DELETE FROM usuarios_perfis WHERE usuario_id=10;


INSERT INTO usuarios(email, senha, nome_completo, nome_usuario, verificado,ativo) VALUES ('rodrigo@email.com', '$2y$10$wXAjl/n68gkHX.hUZVXRS0O8gyfqp0meTbTzaiI559/qqIVxc2zbu', 'Rodrigo', "rodrigo", 1, 1); 
INSERT INTO usuarios(email, senha, nome_completo, nome_usuario, verificado,ativo) VALUES ('joao@email.com', '$2y$10$wXAjl/n68gkHX.hUZVXRS0O8gyfqp0meTbTzaiI559/qqIVxc2zbu', 'João', "jao", 1, 1);           
INSERT INTO usuarios(email, senha, nome_completo, nome_usuario, verificado,ativo) VALUES ('ana@email.com', '$2y$10$wXAjl/n68gkHX.hUZVXRS0O8gyfqp0meTbTzaiI559/qqIVxc2zbu', 'Ana', "ana", 1, 1);

INSERT INTO usuarios_perfis(usuario_id, perfil_id) VALUES(10,4);
