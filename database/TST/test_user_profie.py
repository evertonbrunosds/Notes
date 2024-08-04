from uuid import uuid4

from util.development_test_tool import *
from psycopg2.errors import (
    CheckViolation,
    NotNullViolation,
    StringDataRightTruncation,
    UniqueViolation,
)


# SETTING


@pytest.mark.skip(reason="explicit calls only")
def insert_user_profile(
    cursor: Cursor,
    id: str | None = str(uuid4()),
    user_name: str | None = "username",
    email: str | None = "email@email.domain",
    display_name: str | None = "Display Name",
    description: str | None = "Description Text",
    birthday: str | None = "1990-02-24",
    password: str | None = f"{'*' * 60}",
    created_at: str | None = None,
) -> tuple[Exception | None, str]:
    return insert(
        cursor=cursor,
        table_name="user_profile",
        column_data={
            "id": id,
            "user_name": user_name,
            "email": email,
            "display_name": display_name,
            "description": description,
            "birthday": birthday,
            "password": password,
            "created_at": created_at,
        },
    )


@pytest.mark.skip(reason="explicit calls only")
def select_user_profile(
    cursor: Cursor,
    show_columns: list[str] = ["*"],
    id: str | None = None,
    user_name: str | None = None,
    email: str | None = None,
    display_name: str | None = None,
    description: str | None = None,
    birthday: str | None = None,
    password: str | None = None,
    created_at: str | None = None,
) -> tuple[str | int | None, ...]:
    return select(
        cursor=cursor,
        table_name="user_profile",
        show_columns=show_columns,
        select_conditions={
            "id": id,
            "user_name": user_name,
            "email": email,
            "display_name": display_name,
            "description": description,
            "birthday": birthday,
            "password": password,
            "created_at": created_at,
        },
    )


# -----> STRUCTURE : COLUMNS


def test_id_column_structure(cursor: Cursor) -> None:
    verify_structure(
        expected_table_name="user_profile",
        expected_column_name="id",
        expected_data_type="uuid",
        expected_character_maximum_length=None,
        expected_column_default="uuid_generate_v4()",
        expected_is_nullable=False,
        expected_constraints={ConstraintType.PRIMARY_KEY: None},
        expected_is_index=False,
        cursor=cursor,
    )


def test_user_name_column_structure(cursor: Cursor) -> None:
    verify_structure(
        expected_table_name="user_profile",
        expected_column_name="user_name",
        expected_data_type="character varying",
        expected_character_maximum_length=32,
        expected_column_default=None,
        expected_is_nullable=False,
        expected_constraints={
            ConstraintType.UNIQUE: None,
            ConstraintType.CHECK: "(((user_name)::text ~ '^(?!_)(?!.*__)[a-z0-9_]+(?<!_)$'::text))",
        },
        expected_is_index=True,
        cursor=cursor,
    )


def test_email_column_structure(cursor: Cursor) -> None:
    verify_structure(
        expected_table_name="user_profile",
        expected_column_name="email",
        expected_data_type="character varying",
        expected_character_maximum_length=262,
        expected_column_default=None,
        expected_is_nullable=False,
        expected_constraints={ConstraintType.UNIQUE: None},
        expected_is_index=True,
        cursor=cursor,
    )


def test_display_name_column_structure(cursor: Cursor) -> None:
    verify_structure(
        expected_table_name="user_profile",
        expected_column_name="display_name",
        expected_data_type="character varying",
        expected_character_maximum_length=32,
        expected_column_default=None,
        expected_is_nullable=False,
        expected_constraints={
            ConstraintType.CHECK: "(((display_name)::text ~ '^(?! )(?!.*  )[a-zA-Z0-9À-ÿ ]+(?<! )$'::text))"
        },
        expected_is_index=False,
        cursor=cursor,
    )


def test_description_column_structure(cursor: Cursor) -> None:
    verify_structure(
        expected_table_name="user_profile",
        expected_column_name="description",
        expected_data_type="character varying",
        expected_character_maximum_length=1024,
        expected_column_default=None,
        expected_is_nullable=True,
        expected_constraints=None,
        expected_is_index=False,
        cursor=cursor,
    )


def test_birthday_column_structure(cursor: Cursor) -> None:
    verify_structure(
        expected_table_name="user_profile",
        expected_column_name="birthday",
        expected_data_type="date",
        expected_character_maximum_length=None,
        expected_column_default=None,
        expected_is_nullable=False,
        expected_constraints={
            ConstraintType.CHECK: "((EXTRACT(year FROM age((CURRENT_DATE)::timestamp with time zone, (birthday)::timestamp with time zone)) >= (18)::numeric))",
        },
        expected_is_index=False,
        cursor=cursor,
    )


def test_password_column_structure(cursor: Cursor) -> None:
    verify_structure(
        expected_table_name="user_profile",
        expected_column_name="password",
        expected_data_type="character",
        expected_character_maximum_length=60,
        expected_column_default=None,
        expected_is_nullable=False,
        expected_constraints={
            ConstraintType.CHECK: "((length(password) = 60))",
        },
        expected_is_index=False,
        cursor=cursor,
    )


def test_created_at_column_structure(cursor: Cursor) -> None:
    verify_structure(
        expected_table_name="user_profile",
        expected_column_name="created_at",
        expected_data_type="timestamp with time zone",
        expected_character_maximum_length=None,
        expected_column_default="CURRENT_TIMESTAMP",
        expected_is_nullable=False,
        expected_constraints=None,
        expected_is_index=False,
        cursor=cursor,
    )


# -----> BEHAVIOR : NULL


def test_insert_null_value_in_id_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, id="null")
    assert (
        isinstance(exception, NotNullViolation)
        and '"id"' in message
        and '"user_profile"' in message
    ), message


def test_insert_null_value_in_user_name_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, user_name="null")
    assert (
        isinstance(exception, NotNullViolation)
        and '"user_name"' in message
        and '"user_profile"' in message
    ), message


def test_insert_null_value_in_email_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, email="null")
    assert (
        isinstance(exception, NotNullViolation)
        and '"email"' in message
        and '"user_profile"' in message
    ), message


def test_insert_null_value_in_display_name_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, display_name="null")
    assert (
        isinstance(exception, NotNullViolation)
        and '"display_name"' in message
        and '"user_profile"' in message
    ), message


def test_insert_null_value_in_description_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, description="null")
    assert exception is None and message == "", message


def test_insert_null_value_in_birthday_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, birthday="null")
    assert (
        isinstance(exception, NotNullViolation)
        and '"birthday"' in message
        and '"user_profile"' in message
    ), message


def test_insert_null_value_in_password_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, password="null")
    assert (
        isinstance(exception, NotNullViolation)
        and '"password"' in message
        and '"user_profile"' in message
    ), message


def test_insert_null_value_in_created_at_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, created_at="null")
    assert (
        isinstance(exception, NotNullViolation)
        and '"created_at"' in message
        and '"user_profile"' in message
    ), message


# -----> BEHAVIOR : DEFAULT


def test_insert_default_value_in_id_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, id=None, user_name="somebody")
    assert exception is None and message == "", message
    id, user_name = select_user_profile(
        cursor,
        show_columns=["id", "user_name"],
        user_name="somebody",
    )
    assert id is not None and user_name == "somebody"


def test_insert_default_value_in_user_name_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, user_name=None)
    assert (
        isinstance(exception, NotNullViolation)
        and '"user_name"' in message
        and '"user_profile"' in message
    ), message


def test_insert_default_value_in_email_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, email=None)
    assert (
        isinstance(exception, NotNullViolation)
        and '"email"' in message
        and '"user_profile"' in message
    ), message


def test_insert_default_value_in_display_name_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, display_name=None)
    assert (
        isinstance(exception, NotNullViolation)
        and '"display_name"' in message
        and '"user_profile"' in message
    ), message


def test_insert_default_value_in_description_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor, description=None, user_name="somebody"
    )
    assert exception is None and message == "", message
    description, user_name = select_user_profile(
        cursor,
        show_columns=["description", "user_name"],
        user_name="somebody",
    )
    assert description is None and user_name == "somebody"


def test_insert_default_value_in_birthday_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, birthday=None)
    assert (
        isinstance(exception, NotNullViolation)
        and '"birthday"' in message
        and '"user_profile"' in message
    ), message


def test_insert_default_value_in_password_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, password=None)
    assert (
        isinstance(exception, NotNullViolation)
        and '"password"' in message
        and '"user_profile"' in message
    ), message


def test_insert_default_value_in_created_at_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor, created_at=None, user_name="somebody"
    )
    assert exception is None and message == "", message
    created_at, user_name = select_user_profile(
        cursor,
        show_columns=["created_at", "user_name"],
        user_name="somebody",
    )
    assert created_at is not None and user_name == "somebody"


# -----> BEHAVIOR : LENGTH


def test_insert_hyper_length_value_in_user_name_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, user_name="e" * 33)
    assert (
        isinstance(exception, StringDataRightTruncation)
        and "character varying(32)" in message
    ), message


def test_insert_hyper_length_value_in_email_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, email="e" * 263)
    assert (
        isinstance(exception, StringDataRightTruncation)
        and "character varying(262)" in message
    ), message


def test_insert_hyper_length_value_in_display_name_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, display_name="e" * 33)
    assert (
        isinstance(exception, StringDataRightTruncation)
        and "character varying(32)" in message
    ), message

def test_insert_hyper_length_value_in_description_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, description="e" * 1025)
    assert (
        isinstance(exception, StringDataRightTruncation)
        and "character varying(1024)" in message
    ), message


def test_insert_hyper_length_value_in_password_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, password="e" * 61)
    assert (
        isinstance(exception, StringDataRightTruncation) and "character(60)" in message
    ), message


# -----> BEHAVIOR : UNIQUE


def test_insert_unique_value_in_id_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5000",
        user_name="bona_parte_0",
        email="bona_parte@email.com_0",
        display_name="Bona Parte 0",
        description="Description of The Bona Parte 0",
        birthday="1990-12-30",
        password=f"{'0'*60}",
        created_at="2024-07-10 17:04:18.963277-00",
    )
    assert exception is None and message == "", message
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5000",
        user_name="bona_parte_1",
        email="bona_parte@email.com_1",
        display_name="Bona Parte 1",
        description="Description of The Bona Parte 1",
        birthday="1990-12-31",
        password=f"{'1'*60}",
        created_at="2024-07-10 17:04:18.963277-01",
    )
    assert (
        isinstance(exception, UniqueViolation)
        and "(id)=(3ac4f1a5-75a8-4357-9312-d4a5d04d5000)" in message
    ), message


def test_insert_unique_value_in_user_name_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5000",
        user_name="bona_parte_0",
        email="bona_parte@email.com_0",
        display_name="Bona Parte 0",
        description="Description of The Bona Parte 0",
        birthday="1990-12-30",
        password=f"{'0'*60}",
        created_at="2024-07-10 17:04:18.963277-00",
    )
    assert exception is None and message == "", message
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5001",
        user_name="bona_parte_0",
        email="bona_parte@email.com_1",
        display_name="Bona Parte 1",
        description="Description of The Bona Parte 1",
        birthday="1990-12-31",
        password=f"{'1'*60}",
        created_at="2024-07-10 17:04:18.963277-01",
    )
    assert (
        isinstance(exception, UniqueViolation)
        and "(user_name)=(bona_parte_0)" in message
    ), message


def test_insert_unique_value_in_email_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5000",
        user_name="bona_parte_0",
        email="bona_parte@email.com_0",
        display_name="Bona Parte 0",
        description="Description of The Bona Parte 0",
        birthday="1990-12-30",
        password=f"{'0'*60}",
        created_at="2024-07-10 17:04:18.963277-00",
    )
    assert exception is None and message == "", message
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5001",
        user_name="bona_parte_1",
        email="bona_parte@email.com_0",
        display_name="Bona Parte 1",
        description="Description of The Bona Parte 1",
        birthday="1990-12-31",
        password=f"{'1'*60}",
        created_at="2024-07-10 17:04:18.963277-01",
    )
    assert (
        isinstance(exception, UniqueViolation)
        and "(email)=(bona_parte@email.com_0)" in message
    ), message


def test_insert_unique_value_in_display_name_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5000",
        user_name="bona_parte_0",
        email="bona_parte@email.com_0",
        display_name="Bona Parte 0",
        description="Description of The Bona Parte 0",
        birthday="1990-12-30",
        password=f"{'0'*60}",
        created_at="2024-07-10 17:04:18.963277-00",
    )
    assert exception is None and message == "", message
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5001",
        user_name="bona_parte_1",
        email="bona_parte@email.com_1",
        display_name="Bona Parte 0",
        description="Description of The Bona Parte 1",
        birthday="1990-12-31",
        password=f"{'1'*60}",
        created_at="2024-07-10 17:04:18.963277-01",
    )
    assert exception is None and "" == message, message


def test_insert_unique_value_in_description_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5000",
        user_name="bona_parte_0",
        email="bona_parte@email.com_0",
        display_name="Bona Parte 0",
        description="Description of The Bona Parte 0",
        birthday="1990-12-30",
        password=f"{'0'*60}",
        created_at="2024-07-10 17:04:18.963277-00",
    )
    assert exception is None and message == "", message
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5001",
        user_name="bona_parte_1",
        email="bona_parte@email.com_1",
        display_name="Bona Parte 1",
        description="Description of The Bona Parte 0",
        birthday="1990-12-31",
        password=f"{'1'*60}",
        created_at="2024-07-10 17:04:18.963277-01",
    )
    assert exception is None and "" == message, message


def test_insert_unique_value_in_birthday_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5000",
        user_name="bona_parte_0",
        email="bona_parte@email.com_0",
        display_name="Bona Parte 0",
        description="Description of The Bona Parte 0",
        birthday="1990-12-30",
        password=f"{'0'*60}",
        created_at="2024-07-10 17:04:18.963277-00",
    )
    assert exception is None and message == "", message
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5001",
        user_name="bona_parte_1",
        email="bona_parte@email.com_1",
        display_name="Bona Parte 1",
        description="Description of The Bona Parte 1",
        birthday="1990-12-30",
        password=f"{'1'*60}",
        created_at="2024-07-10 17:04:18.963277-01",
    )
    assert exception is None and "" == message, message


def test_insert_unique_value_in_password_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5000",
        user_name="bona_parte_0",
        email="bona_parte@email.com_0",
        display_name="Bona Parte 0",
        description="Description of The Bona Parte 0",
        birthday="1990-12-30",
        password=f"{'0'*60}",
        created_at="2024-07-10 17:04:18.963277-00",
    )
    assert exception is None and message == "", message
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5001",
        user_name="bona_parte_1",
        email="bona_parte@email.com_1",
        display_name="Bona Parte 1",
        description="Description of The Bona Parte 1",
        birthday="1990-12-31",
        password=f"{'0'*60}",
        created_at="2024-07-10 17:04:18.963277-01",
    )
    assert exception is None and "" == message, message


def test_insert_unique_value_in_created_at_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5000",
        user_name="bona_parte_0",
        email="bona_parte@email.com_0",
        display_name="Bona Parte 0",
        description="Description of The Bona Parte 0",
        birthday="1990-12-30",
        password=f"{'0'*60}",
        created_at="2024-07-10 17:04:18.963277-00",
    )
    assert exception is None and message == "", message
    exception, message = insert_user_profile(
        cursor,
        id="3ac4f1a5-75a8-4357-9312-d4a5d04d5001",
        user_name="bona_parte_1",
        email="bona_parte@email.com_1",
        display_name="Bona Parte 1",
        description="Description of The Bona Parte 1",
        birthday="1990-12-31",
        password=f"{'1'*60}",
        created_at="2024-07-10 17:04:18.963277-00",
    )
    assert exception is None and "" == message, message


# -----> BEHAVIOR : CHECK


def test_insert_check_value_in_user_name_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor,
        user_name="BonaParte",
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_user_name_check"' in message
        and '"user_profile"' in message
    )
    exception, message = insert_user_profile(
        cursor,
        user_name="_bona",
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_user_name_check"' in message
        and '"user_profile"' in message
    )
    exception, message = insert_user_profile(
        cursor,
        user_name="bona_",
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_user_name_check"' in message
        and '"user_profile"' in message
    )
    exception, message = insert_user_profile(
        cursor,
        user_name="bona__parte",
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_user_name_check"' in message
        and '"user_profile"' in message
    )
    exception, message = insert_user_profile(
        cursor,
        user_name="",
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_user_name_check"' in message
        and '"user_profile"' in message
    )
    exception, message = insert_user_profile(
        cursor,
        user_name="napoleão",
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_user_name_check"' in message
        and '"user_profile"' in message
    )


def test_insert_check_value_in_display_name_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(
        cursor,
        display_name=" Bona",
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_display_name_check"' in message
        and '"user_profile"' in message
    )
    exception, message = insert_user_profile(
        cursor,
        display_name="Bona ",
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_display_name_check"' in message
        and '"user_profile"' in message
    )
    exception, message = insert_user_profile(
        cursor,
        display_name="Bona  Parte",
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_display_name_check"' in message
        and '"user_profile"' in message
    )
    exception, message = insert_user_profile(
        cursor,
        display_name="",
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_display_name_check"' in message
        and '"user_profile"' in message
    )
    exception, message = insert_user_profile(
        cursor,
        display_name="Napoleão Bona Parte",
    )
    assert exception is None and message == "", message


def test_insert_check_value_in_birthday_column(cursor: Cursor) -> None:
    today = datetime.date.today()
    date_under_eighteen = today.replace(year=today.year - 17)
    exception, message = insert_user_profile(
        cursor,
        birthday=date_under_eighteen.strftime("%Y-%m-%d"),
    )
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_birthday_check"' in message
        and '"user_profile"' in message
    ), message


def test_insert_check_value_in_password_column(cursor: Cursor) -> None:
    exception, message = insert_user_profile(cursor, password=f"{'x'*59}")
    assert (
        isinstance(exception, CheckViolation)
        and '"user_profile_password_check"' in message
        and '"user_profile"' in message
    ), message
