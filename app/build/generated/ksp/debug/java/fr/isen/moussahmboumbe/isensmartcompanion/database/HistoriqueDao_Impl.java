package fr.isen.moussahmboumbe.isensmartcompanion.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import fr.isen.moussahmboumbe.isensmartcompanion.model.HistoriqueItem;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HistoriqueDao_Impl implements HistoriqueDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HistoriqueItem> __insertionAdapterOfHistoriqueItem;

  private final EntityDeletionOrUpdateAdapter<HistoriqueItem> __deletionAdapterOfHistoriqueItem;

  private final SharedSQLiteStatement __preparedStmtOfClearHistorique;

  public HistoriqueDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHistoriqueItem = new EntityInsertionAdapter<HistoriqueItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `historique` (`id`,`question`,`reponse`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoriqueItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getQuestion());
        statement.bindString(3, entity.getReponse());
      }
    };
    this.__deletionAdapterOfHistoriqueItem = new EntityDeletionOrUpdateAdapter<HistoriqueItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `historique` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoriqueItem entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfClearHistorique = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM historique";
        return _query;
      }
    };
  }

  @Override
  public Object insertHistorique(final HistoriqueItem historiqueItem,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHistoriqueItem.insert(historiqueItem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHistorique(final HistoriqueItem historiqueItem,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHistoriqueItem.handle(historiqueItem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearHistorique(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearHistorique.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearHistorique.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HistoriqueItem>> getAllHistorique() {
    final String _sql = "SELECT * FROM historique ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"historique"}, new Callable<List<HistoriqueItem>>() {
      @Override
      @NonNull
      public List<HistoriqueItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
          final int _cursorIndexOfReponse = CursorUtil.getColumnIndexOrThrow(_cursor, "reponse");
          final List<HistoriqueItem> _result = new ArrayList<HistoriqueItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoriqueItem _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpQuestion;
            _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
            final String _tmpReponse;
            _tmpReponse = _cursor.getString(_cursorIndexOfReponse);
            _item = new HistoriqueItem(_tmpId,_tmpQuestion,_tmpReponse);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
