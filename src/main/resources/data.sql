INSERT INTO public."role"
(id, activo, nombre)
VALUES(1, true, 'Presidente');

INSERT INTO public."role"
(id, activo, nombre)
VALUES(2, true, 'Secretario');

INSERT INTO public."role"
(id, activo, nombre)
VALUES(3, true, 'Tesorero');

INSERT INTO public."role"
(id, activo, nombre)
VALUES(4, true, 'Vocal');

INSERT INTO public.ciudadanos
(id,activo, curp, descripcion, direccion, fecha_actualizacion, fecha_creacion, fecha_nacimiento, nombre, usuario_editor)
VALUES(0, true, 'AAAA000000HHHHHH00', NULL, NULL, NULL, now(), now(), 'Administrador de sistema', null);

INSERT INTO public.users
(id, active, user_name, "password", ciudadano_id)
VALUES(0, true, 'admin', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 0);


INSERT INTO public.comitivas
(id, activo, fecha_actualizacion, fecha_creacion, nombre, usuario_editor)
VALUES(0, true, null, now(), 'Administrador', null);

INSERT INTO public.administradores
(id, activo, fecha_actualizacion, fecha_creacion, role_id, usuario_editor, ciudadano_id, comitiva_id)
VALUES(0, true, null, now(), 1, null, 0, 0);
